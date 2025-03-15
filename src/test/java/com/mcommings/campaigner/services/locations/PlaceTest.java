package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.locations.dtos.PlaceDTO;
import com.mcommings.campaigner.modules.locations.entities.Place;
import com.mcommings.campaigner.modules.locations.mappers.PlaceMapper;
import com.mcommings.campaigner.modules.locations.repositories.IPlaceRepository;
import com.mcommings.campaigner.modules.locations.services.PlaceService;
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
public class PlaceTest {

    @Mock
    private PlaceMapper placeMapper;

    @Mock
    private IPlaceRepository placeRepository;

    @InjectMocks
    private PlaceService placeService;

    private Place entity;
    private PlaceDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Place();
        entity.setId(1);
        entity.setName("Test Place");
        entity.setDescription("A fictional place.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_place_type(random.nextInt(100) + 1);
        entity.setFk_terrain(random.nextInt(100) + 1);
        entity.setFk_country(random.nextInt(100) + 1);
        entity.setFk_city(random.nextInt(100) + 1);
        entity.setFk_region(random.nextInt(100) + 1);

        dto = new PlaceDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_place_type(entity.getFk_place_type());
        dto.setFk_terrain(entity.getFk_terrain());
        dto.setFk_country(entity.getFk_country());
        dto.setFk_city(entity.getFk_city());
        dto.setFk_region(entity.getFk_region());

        when(placeMapper.mapToPlaceDto(entity)).thenReturn(dto);
        when(placeMapper.mapFromPlaceDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereArePlaces_getPlaces_ReturnsPlaces() {
        when(placeRepository.findAll()).thenReturn(List.of(entity));
        List<PlaceDTO> result = placeService.getPlaces();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Place", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoPlaces_getPlaces_ReturnsEmptyList() {
        when(placeRepository.findAll()).thenReturn(Collections.emptyList());

        List<PlaceDTO> result = placeService.getPlaces();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no places.");
    }

    @Test
    void whenThereIsAPlace_getPlace_ReturnsPlaceById() {
        when(placeRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<PlaceDTO> result = placeService.getPlace(1);

        assertTrue(result.isPresent());
        assertEquals("Test Place", result.get().getName());
    }

    @Test
    void whenThereIsNotAPlace_getPlace_ReturnsNothing() {
        when(placeRepository.findById(999)).thenReturn(Optional.empty());

        Optional<PlaceDTO> result = placeService.getPlace(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when place is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getPlacesByCampaignUUID_ReturnsPlaces() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(placeRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<PlaceDTO> result = placeService.getPlacesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getPlacesByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(placeRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<PlaceDTO> result = placeService.getPlacesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no places match the campaign UUID.");
    }

    @Test
    void whenPlaceIsValid_savePlace_SavesThePlace() {
        when(placeRepository.save(entity)).thenReturn(entity);

        placeService.savePlace(dto);

        verify(placeRepository, times(1)).save(entity);
    }

    @Test
    public void whenPlaceNameIsInvalid_savePlace_ThrowsIllegalArgumentException() {
        PlaceDTO placeWithEmptyName = new PlaceDTO();
        placeWithEmptyName.setId(1);
        placeWithEmptyName.setName("");
        placeWithEmptyName.setDescription("A fictional place.");
        placeWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());
        placeWithEmptyName.setFk_place_type(1);
        placeWithEmptyName.setFk_terrain(1);
        placeWithEmptyName.setFk_country(1);
        placeWithEmptyName.setFk_city(1);
        placeWithEmptyName.setFk_region(1);

        PlaceDTO placeWithNullName = new PlaceDTO();
        placeWithNullName.setId(1);
        placeWithNullName.setName(null);
        placeWithNullName.setDescription("A fictional place.");
        placeWithNullName.setFk_campaign_uuid(UUID.randomUUID());
        placeWithNullName.setFk_place_type(1);
        placeWithNullName.setFk_terrain(1);
        placeWithNullName.setFk_country(1);
        placeWithNullName.setFk_city(1);
        placeWithNullName.setFk_region(1);

        assertThrows(IllegalArgumentException.class, () -> placeService.savePlace(placeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> placeService.savePlace(placeWithNullName));
    }

    @Test
    public void whenPlaceNameAlreadyExists_savePlace_ThrowsDataIntegrityViolationException() {
        when(placeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> placeService.savePlace(dto));
        verify(placeRepository, times(1)).findByName(dto.getName());
        verify(placeRepository, never()).save(any(Place.class));
    }

    @Test
    void whenPlaceIdExists_deletePlace_DeletesThePlace() {
        when(placeRepository.existsById(1)).thenReturn(true);
        placeService.deletePlace(1);
        verify(placeRepository, times(1)).deleteById(1);
    }

    @Test
    void whenPlaceIdDoesNotExist_deletePlace_ThrowsIllegalArgumentException() {
        when(placeRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> placeService.deletePlace(999));
    }

    @Test
    void whenDeletePlaceFails_deletePlace_ThrowsException() {
        when(placeRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(placeRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> placeService.deletePlace(1));
    }

    @Test
    void whenPlaceIdIsFound_updatePlace_UpdatesThePlace() {
        PlaceDTO updateDTO = new PlaceDTO();
        updateDTO.setName("Updated Name");

        when(placeRepository.findById(1)).thenReturn(Optional.of(entity));
        when(placeRepository.existsById(1)).thenReturn(true);
        when(placeRepository.save(entity)).thenReturn(entity);
        when(placeMapper.mapToPlaceDto(entity)).thenReturn(updateDTO);

        Optional<PlaceDTO> result = placeService.updatePlace(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenPlaceIdIsNotFound_updatePlace_ReturnsEmptyOptional() {
        PlaceDTO updateDTO = new PlaceDTO();
        updateDTO.setName("Updated Name");

        when(placeRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> placeService.updatePlace(999, updateDTO));
    }

    @Test
    public void whenPlaceNameIsInvalid_updatePlace_ThrowsIllegalArgumentException() {
        PlaceDTO updateEmptyName = new PlaceDTO();
        updateEmptyName.setName("");

        PlaceDTO updateNullName = new PlaceDTO();
        updateNullName.setName(null);

        when(placeRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> placeService.updatePlace(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> placeService.updatePlace(1, updateNullName));
    }

    @Test
    public void whenPlaceNameAlreadyExists_updatePlace_ThrowsDataIntegrityViolationException() {
        when(placeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> placeService.updatePlace(entity.getId(), dto));
    }
}
