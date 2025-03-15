package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.locations.dtos.ContinentDTO;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.modules.locations.mappers.ContinentMapper;
import com.mcommings.campaigner.modules.locations.repositories.IContinentRepository;
import com.mcommings.campaigner.modules.locations.services.ContinentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ContinentTest {

    @Mock
    private ContinentMapper continentMapper;

    @Mock
    private IContinentRepository continentRepository;

    @InjectMocks
    private ContinentService continentService;

    private Continent entity;
    private ContinentDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Continent();
        entity.setId(1);
        entity.setName("Test Continent");
        entity.setDescription("A fictional land.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new ContinentDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());

        // Mocking the mapper behavior
        when(continentMapper.mapToContinentDto(entity)).thenReturn(dto);
        when(continentMapper.mapFromContinentDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreContinents_getContinents_ReturnsContinents() {
        when(continentRepository.findAll()).thenReturn(List.of(entity));
        List<ContinentDTO> result = continentService.getContinents();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Continent", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoContinents_getContinents_ReturnsEmptyList() {
        when(continentRepository.findAll()).thenReturn(Collections.emptyList());

        List<ContinentDTO> result = continentService.getContinents();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no continents.");
    }

    @Test
    void getContinent_ReturnsContinentById() {
        when(continentRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<ContinentDTO> result = continentService.getContinent(1);

        assertTrue(result.isPresent());
        assertEquals("Test Continent", result.get().getName());
    }

    @Test
    void whenThereIsNotAContinent_getContinent_ReturnsNothing() {
        when(continentRepository.findById(999)).thenReturn(Optional.empty());

        Optional<ContinentDTO> result = continentService.getContinent(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when continent is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getContinentsByCampaignUUID_ReturnsContinents() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(continentRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<ContinentDTO> result = continentService.getContinentsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getContinentsByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(continentRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<ContinentDTO> result = continentService.getContinentsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no continents match the campaign UUID.");
    }

    @Test
    void whenContinentIsValid_saveContinent_SavesTheContinent() {
        when(continentRepository.save(entity)).thenReturn(entity);

        continentService.saveContinent(dto);

        verify(continentRepository, times(1)).save(entity);
    }

    @Test
    public void whenContinentNameIsInvalid_saveContinent_ThrowsIllegalArgumentException() {
        ContinentDTO continentWithEmptyName = new ContinentDTO();
        continentWithEmptyName.setId(1);
        continentWithEmptyName.setName("");
        continentWithEmptyName.setDescription("A fictional continent.");
        continentWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());

        ContinentDTO continentWithNullName = new ContinentDTO();
        continentWithNullName.setId(1);
        continentWithNullName.setName(null);
        continentWithNullName.setDescription("A fictional continent.");
        continentWithNullName.setFk_campaign_uuid(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> continentService.saveContinent(continentWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> continentService.saveContinent(continentWithNullName));
    }

    @Test
    public void whenContinentNameAlreadyExists_saveContinent_ThrowsDataIntegrityViolationException() {
        when(continentRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> continentService.saveContinent(dto));
        verify(continentRepository, times(1)).findByName(dto.getName());
        verify(continentRepository, never()).save(any(Continent.class));
    }

    @Test
    void whenContinentIdExists_deleteContinent_DeletesTheContinent() {
        when(continentRepository.existsById(1)).thenReturn(true);
        continentService.deleteContinent(1);
        verify(continentRepository, times(1)).deleteById(1);
    }

    @Test
    void whenContinentIdDoesNotExist_deleteContinent_ThrowsIllegalArgumentException() {
        when(continentRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> continentService.deleteContinent(999));
    }

    @Test
    void whenDeleteContinentFails_deleteContinent_ThrowsException() {
        when(continentRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(continentRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> continentService.deleteContinent(1));
    }

    @Test
    void whenContinentIdIsFound_updateContinent_UpdatesTheContinent() {
        ContinentDTO updateDTO = new ContinentDTO();
        updateDTO.setName("Updated Name");

        when(continentRepository.findById(1)).thenReturn(Optional.of(entity));
        when(continentRepository.existsById(1)).thenReturn(true);
        when(continentRepository.save(entity)).thenReturn(entity);
        when(continentMapper.mapToContinentDto(entity)).thenReturn(updateDTO);

        Optional<ContinentDTO> result = continentService.updateContinent(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenContinentIdIsNotFound_updateContinent_ReturnsEmptyOptional() {
        ContinentDTO updateDTO = new ContinentDTO();
        updateDTO.setName("Updated Name");

        when(continentRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> continentService.updateContinent(999, updateDTO));
    }

    @Test
    public void whenContinentNameIsInvalid_updateContinent_ThrowsIllegalArgumentException() {
        ContinentDTO updateEmptyName = new ContinentDTO();
        updateEmptyName.setName("");

        ContinentDTO updateNullName = new ContinentDTO();
        updateNullName.setName(null);

        when(continentRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> continentService.updateContinent(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> continentService.updateContinent(1, updateNullName));
    }

    @Test
    public void whenContinentNameAlreadyExists_updateContinent_ThrowsDataIntegrityViolationException() {
        when(continentRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> continentService.updateContinent(entity.getId(), dto));
    }
}