package com.mcommings.campaigner.services.locations;


import com.mcommings.campaigner.modules.locations.dtos.CountryDTO;
import com.mcommings.campaigner.modules.locations.entities.Country;
import com.mcommings.campaigner.modules.locations.mappers.CountryMapper;
import com.mcommings.campaigner.modules.locations.repositories.ICountryRepository;
import com.mcommings.campaigner.modules.locations.services.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CountryTest {

    @Mock
    private CountryMapper countryMapper;

    @Mock
    private ICountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    private Country entity;
    private CountryDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Country();
        entity.setId(1);
        entity.setName("Test Country");
        entity.setDescription("A fictional country.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_continent(random.nextInt(100) + 1);
        entity.setFk_government(random.nextInt(100) + 1);

        dto = new CountryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_continent(entity.getFk_continent());
        dto.setFk_government(entity.getFk_government());

        when(countryMapper.mapToCountryDto(entity)).thenReturn(dto);
        when(countryMapper.mapFromCountryDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreCountries_getCountries_ReturnsCountries() {
        when(countryRepository.findAll()).thenReturn(List.of(entity));
        List<CountryDTO> result = countryService.getCountries();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Country", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoCountries_getCountries_ReturnsNothing() {
        when(countryRepository.findAll()).thenReturn(Collections.emptyList());

        List<CountryDTO> result = countryService.getCountries();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no countries.");
    }

    @Test
    public void whenThereIsACountry_getCountry_ReturnsCountry() {
        when(countryRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<CountryDTO> result = countryService.getCountry(1);

        assertTrue(result.isPresent());
        assertEquals("Test Country", result.get().getName());
    }

    @Test
    public void whenThereIsNotACountry_getCountry_ReturnsCountry() {
        when(countryRepository.findById(999)).thenReturn(Optional.empty());

        Optional<CountryDTO> result = countryService.getCountry(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when country is not found.");
    }

    @Test
    public void whenCampaignUUIDIsValid_getCountriesByCampaignUUID_ReturnsCountries() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(countryRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<CountryDTO> result = countryService.getCountriesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getCountriesByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(countryRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<CountryDTO> result = countryService.getCountriesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no countries match the campaign UUID.");
    }

    @Test
    public void whenCountryIsValid_saveCountry_SavesTheCountry() {
        when(countryRepository.save(entity)).thenReturn(entity);

        countryService.saveCountry(dto);

        verify(countryRepository, times(1)).save(entity);
    }

    @Test
    public void whenCountryNameIsInvalid_saveCountry_ThrowsIllegalArgumentException() {
        CountryDTO countryWithEmptyName = new CountryDTO();
        countryWithEmptyName.setId(1);
        countryWithEmptyName.setName("");
        countryWithEmptyName.setDescription("A fictional country.");
        countryWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());
        countryWithEmptyName.setFk_continent(1);
        countryWithEmptyName.setFk_government(1);

        CountryDTO countryWithNullName = new CountryDTO();
        countryWithNullName.setId(1);
        countryWithNullName.setName(null);
        countryWithNullName.setDescription("A fictional city.");
        countryWithNullName.setFk_campaign_uuid(UUID.randomUUID());
        countryWithNullName.setFk_continent(1);
        countryWithNullName.setFk_government(1);

        assertThrows(IllegalArgumentException.class, () -> countryService.saveCountry(countryWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> countryService.saveCountry(countryWithNullName));
    }

    @Test
    public void whenCountryNameAlreadyExists_saveCountry_ThrowsDataIntegrityViolationException() {
        when(countryRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> countryService.saveCountry(dto));
        verify(countryRepository, times(1)).findByName(dto.getName());
        verify(countryRepository, never()).save(any(Country.class));
    }

    @Test
    public void whenCountryIdExists_deleteCountry_DeletesTheCountry() {
        when(countryRepository.existsById(1)).thenReturn(true);
        countryService.deleteCountry(1);
        verify(countryRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenCountryIdDoesNotExist_deleteCountry_ThrowsIllegalArgumentException() {
        when(countryRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> countryService.deleteCountry(999));
    }

    @Test
    public void whenDeleteCountryFails_deleteCountry_ThrowsException() {
        when(countryRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(countryRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> countryService.deleteCountry(1));
    }

    @Test
    public void whenCountryIdIsFound_updateCountry_UpdatesTheCountry() {
        CountryDTO updateDTO = new CountryDTO();
        updateDTO.setName("Updated Name");

        when(countryRepository.findById(1)).thenReturn(Optional.of(entity));
        when(countryRepository.existsById(1)).thenReturn(true);
        when(countryRepository.save(entity)).thenReturn(entity);
        when(countryMapper.mapToCountryDto(entity)).thenReturn(updateDTO);

        Optional<CountryDTO> result = countryService.updateCountry(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    public void whenCountryIdIsNotFound_updateCountry_ReturnsEmptyOptional() {
        CountryDTO updateDTO = new CountryDTO();
        updateDTO.setName("Updated Name");

        when(countryRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> countryService.updateCountry(999, updateDTO));
    }

    @Test
    public void whenCountryNameIsInvalid_updateCountry_ThrowsIllegalArgumentException() {
        CountryDTO updateEmptyName = new CountryDTO();
        updateEmptyName.setName("");

        CountryDTO updateNullName = new CountryDTO();
        updateNullName.setName(null);

        when(countryRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> countryService.updateCountry(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> countryService.updateCountry(1, updateNullName));
    }

    @Test
    public void whenCountryNameAlreadyExists_updateCountry_ThrowsDataIntegrityViolationException() {
        when(countryRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> countryService.updateCountry(entity.getId(), dto));
    }

}
