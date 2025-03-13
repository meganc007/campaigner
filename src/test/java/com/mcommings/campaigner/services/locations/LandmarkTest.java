package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.locations.dtos.LandmarkDTO;
import com.mcommings.campaigner.locations.entities.Landmark;
import com.mcommings.campaigner.locations.mappers.LandmarkMapper;
import com.mcommings.campaigner.locations.repositories.ILandmarkRepository;
import com.mcommings.campaigner.locations.services.LandmarkService;
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
public class LandmarkTest {

    @Mock
    private LandmarkMapper landmarkMapper;

    @Mock
    private ILandmarkRepository landmarkRepository;

    @InjectMocks
    private LandmarkService landmarkService;

    private Landmark entity;
    private LandmarkDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Landmark();
        entity.setId(1);
        entity.setName("Test Landmark");
        entity.setDescription("A fictional landmark.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_region(random.nextInt(100) + 1);

        dto = new LandmarkDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_region(entity.getFk_region());

        // Mocking the mapper behavior
        when(landmarkMapper.mapToLandmarkDto(entity)).thenReturn(dto);
        when(landmarkMapper.mapFromLandmarkDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreLandmarks_getLandmarks_ReturnsLandmarks() {
        when(landmarkRepository.findAll()).thenReturn(List.of(entity));
        List<LandmarkDTO> result = landmarkService.getLandmarks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Landmark", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoLandmarks_getLandmarks_ReturnsEmptyList() {
        when(landmarkRepository.findAll()).thenReturn(Collections.emptyList());

        List<LandmarkDTO> result = landmarkService.getLandmarks();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no landmarks.");
    }

    @Test
    void getLandmark_ReturnsLandmarkById() {
        when(landmarkRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<LandmarkDTO> result = landmarkService.getLandmark(1);

        assertTrue(result.isPresent());
        assertEquals("Test Landmark", result.get().getName());
    }

    @Test
    void whenThereIsNotALandmark_getLandmark_ReturnsNothing() {
        when(landmarkRepository.findById(999)).thenReturn(Optional.empty());

        Optional<LandmarkDTO> result = landmarkService.getLandmark(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when landmark is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getLandmarksByCampaignUUID_ReturnsLandmarks() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(landmarkRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<LandmarkDTO> result = landmarkService.getLandmarksByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getLandmarksByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(landmarkRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<LandmarkDTO> result = landmarkService.getLandmarksByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no landmarks match the campaign UUID.");
    }

    @Test
    void whenLandmarkIsValid_saveLandmark_SavesTheLandmark() {
        when(landmarkRepository.save(entity)).thenReturn(entity);

        landmarkService.saveLandmark(dto);

        verify(landmarkRepository, times(1)).save(entity);
    }

    @Test
    public void whenLandmarkNameIsInvalid_saveLandmark_ThrowsIllegalArgumentException() {
        LandmarkDTO landmarkWithEmptyName = new LandmarkDTO();
        landmarkWithEmptyName.setId(1);
        landmarkWithEmptyName.setName("");
        landmarkWithEmptyName.setDescription("A fictional landmark.");
        landmarkWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());

        LandmarkDTO landmarkWithNullName = new LandmarkDTO();
        landmarkWithNullName.setId(1);
        landmarkWithNullName.setName(null);
        landmarkWithNullName.setDescription("A fictional landmark.");
        landmarkWithNullName.setFk_campaign_uuid(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> landmarkService.saveLandmark(landmarkWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> landmarkService.saveLandmark(landmarkWithNullName));
    }

    @Test
    public void whenLandmarkNameAlreadyExists_saveLandmark_ThrowsDataIntegrityViolationException() {
        when(landmarkRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> landmarkService.saveLandmark(dto));
        verify(landmarkRepository, times(1)).findByName(dto.getName());
        verify(landmarkRepository, never()).save(any(Landmark.class));
    }

    @Test
    void whenLandmarkIdExists_deleteLandmark_DeletesTheLandmark() {
        when(landmarkRepository.existsById(1)).thenReturn(true);
        landmarkService.deleteLandmark(1);
        verify(landmarkRepository, times(1)).deleteById(1);
    }

    @Test
    void whenLandmarkIdDoesNotExist_deleteLandmark_ThrowsIllegalArgumentException() {
        when(landmarkRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> landmarkService.deleteLandmark(999));
    }

    @Test
    void whenDeleteLandmarkFails_deleteLandmark_ThrowsException() {
        when(landmarkRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(landmarkRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> landmarkService.deleteLandmark(1));
    }

    @Test
    void whenLandmarkIdIsFound_updateLandmark_UpdatesTheLandmark() {
        LandmarkDTO updateDTO = new LandmarkDTO();
        updateDTO.setName("Updated Name");

        when(landmarkRepository.findById(1)).thenReturn(Optional.of(entity));
        when(landmarkRepository.existsById(1)).thenReturn(true);
        when(landmarkRepository.save(entity)).thenReturn(entity);
        when(landmarkMapper.mapToLandmarkDto(entity)).thenReturn(updateDTO);

        Optional<LandmarkDTO> result = landmarkService.updateLandmark(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenLandmarkIdIsNotFound_updateLandmark_ReturnsEmptyOptional() {
        LandmarkDTO updateDTO = new LandmarkDTO();
        updateDTO.setName("Updated Name");

        when(landmarkRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> landmarkService.updateLandmark(999, updateDTO));
    }

    @Test
    public void whenLandmarkNameIsInvalid_updateLandmark_ThrowsIllegalArgumentException() {
        LandmarkDTO updateEmptyName = new LandmarkDTO();
        updateEmptyName.setName("");

        LandmarkDTO updateNullName = new LandmarkDTO();
        updateNullName.setName(null);

        when(landmarkRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> landmarkService.updateLandmark(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> landmarkService.updateLandmark(1, updateNullName));
    }

    @Test
    public void whenLandmarkNameAlreadyExists_updateLandmark_ThrowsDataIntegrityViolationException() {
        when(landmarkRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> landmarkService.updateLandmark(entity.getId(), dto));
    }
}
