package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.locations.dtos.CityDTO;
import com.mcommings.campaigner.locations.entities.City;
import com.mcommings.campaigner.locations.mappers.CityMapper;
import com.mcommings.campaigner.locations.repositories.ICityRepository;
import com.mcommings.campaigner.locations.services.CityService;
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
public class CityTest {

    @Mock
    private CityMapper cityMapper;

    @Mock
    private ICityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    private City entity;
    private CityDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new City();
        entity.setId(1);
        entity.setName("Test City");
        entity.setDescription("A fictional city.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_wealth(random.nextInt(100) + 1);
        entity.setFk_country(random.nextInt(100) + 1);
        entity.setFk_settlement(random.nextInt(100) + 1);
        entity.setFk_government(random.nextInt(100) + 1);
        entity.setFk_region(random.nextInt(100) + 1);

        dto = new CityDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_wealth(entity.getFk_wealth());
        dto.setFk_country(entity.getFk_country());
        dto.setFk_settlement(entity.getFk_settlement());
        dto.setFk_government(entity.getFk_government());
        dto.setFk_region(entity.getFk_region());

        when(cityMapper.mapToCityDto(entity)).thenReturn(dto);
        when(cityMapper.mapFromCityDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreCities_getCities_ReturnsCities() {
        when(cityRepository.findAll()).thenReturn(List.of(entity));
        List<CityDTO> result = cityService.getCities();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test City", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoCities_getCities_ReturnsEmptyList() {
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());

        List<CityDTO> result = cityService.getCities();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no cities.");
    }

    @Test
    void whenThereIsACity_getCity_ReturnsCityById() {
        when(cityRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<CityDTO> result = cityService.getCity(1);

        assertTrue(result.isPresent());
        assertEquals("Test City", result.get().getName());
    }

    @Test
    void whenThereIsNotACity_getCity_ReturnsNothing() {
        when(cityRepository.findById(999)).thenReturn(Optional.empty());

        Optional<CityDTO> result = cityService.getCity(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when city is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getCitiesByCampaignUUID_ReturnsCities() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(cityRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<CityDTO> result = cityService.getCitiesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getCitiesByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(cityRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<CityDTO> result = cityService.getCitiesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no cities match the campaign UUID.");
    }

    @Test
    void whenCityIsValid_saveCity_SavesTheCity() {
        when(cityRepository.save(entity)).thenReturn(entity);

        cityService.saveCity(dto);

        verify(cityRepository, times(1)).save(entity);
    }

    @Test
    public void whenCityNameIsInvalid_saveCity_ThrowsIllegalArgumentException() {
        CityDTO cityWithEmptyName = new CityDTO();
        cityWithEmptyName.setId(1);
        cityWithEmptyName.setName("");
        cityWithEmptyName.setDescription("A fictional city.");
        cityWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());
        cityWithEmptyName.setFk_wealth(1);
        cityWithEmptyName.setFk_country(1);
        cityWithEmptyName.setFk_settlement(1);
        cityWithEmptyName.setFk_government(1);
        cityWithEmptyName.setFk_region(1);

        CityDTO cityWithNullName = new CityDTO();
        cityWithNullName.setId(1);
        cityWithNullName.setName(null);
        cityWithNullName.setDescription("A fictional city.");
        cityWithNullName.setFk_campaign_uuid(UUID.randomUUID());
        cityWithNullName.setFk_wealth(1);
        cityWithNullName.setFk_country(1);
        cityWithNullName.setFk_settlement(1);
        cityWithNullName.setFk_government(1);
        cityWithNullName.setFk_region(1);

        assertThrows(IllegalArgumentException.class, () -> cityService.saveCity(cityWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> cityService.saveCity(cityWithNullName));
    }

    @Test
    public void whenCityNameAlreadyExists_saveCity_ThrowsDataIntegrityViolationException() {
        when(cityRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> cityService.saveCity(dto));
        verify(cityRepository, times(1)).findByName(dto.getName());
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void whenCityIdExists_deleteCity_DeletesTheCity() {
        when(cityRepository.existsById(1)).thenReturn(true);
        cityService.deleteCity(1);
        verify(cityRepository, times(1)).deleteById(1);
    }

    @Test
    void whenCityIdDoesNotExist_deleteCity_ThrowsIllegalArgumentException() {
        when(cityRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> cityService.deleteCity(999));
    }

    @Test
    void whenDeleteCityFails_deleteCity_ThrowsException() {
        when(cityRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(cityRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> cityService.deleteCity(1));
    }

    @Test
    void whenCityIdIsFound_updateCity_UpdatesTheCity() {
        CityDTO updateDTO = new CityDTO();
        updateDTO.setName("Updated Name");

        when(cityRepository.findById(1)).thenReturn(Optional.of(entity));
        when(cityRepository.existsById(1)).thenReturn(true);
        when(cityRepository.save(entity)).thenReturn(entity);
        when(cityMapper.mapToCityDto(entity)).thenReturn(updateDTO);

        Optional<CityDTO> result = cityService.updateCity(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenCityIdIsNotFound_updateCity_ReturnsEmptyOptional() {
        CityDTO updateDTO = new CityDTO();
        updateDTO.setName("Updated Name");

        when(cityRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> cityService.updateCity(999, updateDTO));
    }

    @Test
    public void whenCityNameIsInvalid_updateCity_ThrowsIllegalArgumentException() {
        CityDTO updateEmptyName = new CityDTO();
        updateEmptyName.setName("");

        CityDTO updateNullName = new CityDTO();
        updateNullName.setName(null);

        when(cityRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> cityService.updateCity(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> cityService.updateCity(1, updateNullName));
    }

    @Test
    public void whenCityNameAlreadyExists_updateCity_ThrowsDataIntegrityViolationException() {
        when(cityRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> cityService.updateCity(entity.getId(), dto));
    }
}
