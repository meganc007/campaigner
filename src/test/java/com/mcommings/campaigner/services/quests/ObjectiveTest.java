package com.mcommings.campaigner.services.quests;

import com.mcommings.campaigner.modules.quests.dtos.ObjectiveDTO;
import com.mcommings.campaigner.modules.quests.entities.Objective;
import com.mcommings.campaigner.modules.quests.mappers.ObjectiveMapper;
import com.mcommings.campaigner.modules.quests.repositories.IObjectiveRepository;
import com.mcommings.campaigner.modules.quests.services.ObjectiveService;
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
public class ObjectiveTest {

    @Mock
    private ObjectiveMapper objectiveMapper;

    @Mock
    private IObjectiveRepository objectiveRepository;

    @InjectMocks
    private ObjectiveService objectiveService;

    private Objective entity;
    private ObjectiveDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Objective();
        entity.setId(1);
        entity.setDescription("This is a description.");
        entity.setNotes("This is a note.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new ObjectiveDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());

        when(objectiveMapper.mapToObjectiveDto(entity)).thenReturn(dto);
        when(objectiveMapper.mapFromObjectiveDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreObjectives_getObjectives_ReturnsObjectives() {
        when(objectiveRepository.findAll()).thenReturn(List.of(entity));
        List<ObjectiveDTO> result = objectiveService.getObjectives();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("This is a description.", result.get(0).getDescription());
    }

    @Test
    public void whenThereAreNoObjectives_getObjectives_ReturnsNothing() {
        when(objectiveRepository.findAll()).thenReturn(Collections.emptyList());

        List<ObjectiveDTO> result = objectiveService.getObjectives();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no objectives.");
    }

    @Test
    public void whenThereIsAObjective_getObjective_ReturnsObjective() {
        when(objectiveRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<ObjectiveDTO> result = objectiveService.getObjective(1);

        assertTrue(result.isPresent());
        assertEquals("This is a description.", result.get().getDescription());
    }

    @Test
    public void whenThereIsNotAObjective_getObjective_ReturnsObjective() {
        when(objectiveRepository.findById(999)).thenReturn(Optional.empty());

        Optional<ObjectiveDTO> result = objectiveService.getObjective(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when objective is not found.");
    }

    @Test
    public void whenCampaignUUIDIsValid_getObjectivesByCampaignUUID_ReturnsObjectives() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(objectiveRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<ObjectiveDTO> result = objectiveService.getObjectivesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getObjectivesByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(objectiveRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<ObjectiveDTO> result = objectiveService.getObjectivesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no objectives match the campaign UUID.");
    }

    @Test
    public void whenObjectiveIsValid_saveObjective_SavesTheObjective() {
        when(objectiveRepository.save(entity)).thenReturn(entity);

        objectiveService.saveObjective(dto);

        verify(objectiveRepository, times(1)).save(entity);
    }

    @Test
    public void whenObjectiveDescriptionIsInvalid_saveObjective_ThrowsIllegalArgumentException() {
        ObjectiveDTO objectiveWithEmptyDescription = new ObjectiveDTO();
        objectiveWithEmptyDescription.setId(1);
        objectiveWithEmptyDescription.setDescription("");
        objectiveWithEmptyDescription.setFk_campaign_uuid(UUID.randomUUID());

        ObjectiveDTO objectiveWithNullDescription = new ObjectiveDTO();
        objectiveWithNullDescription.setId(1);
        objectiveWithNullDescription.setDescription(null);
        objectiveWithNullDescription.setFk_campaign_uuid(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> objectiveService.saveObjective(objectiveWithEmptyDescription));
        assertThrows(IllegalArgumentException.class, () -> objectiveService.saveObjective(objectiveWithNullDescription));
    }

    @Test
    public void whenObjectiveDescriptionAlreadyExists_saveObjective_ThrowsDataIntegrityViolationException() {
        when(objectiveRepository.objectiveExists(objectiveMapper.mapFromObjectiveDto(dto))).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> objectiveService.saveObjective(dto));
        verify(objectiveRepository, times(1)).objectiveExists(objectiveMapper.mapFromObjectiveDto(dto));
        verify(objectiveRepository, never()).save(any(Objective.class));
    }

    @Test
    public void whenObjectiveIdExists_deleteObjective_DeletesTheObjective() {
        when(objectiveRepository.existsById(1)).thenReturn(true);
        objectiveService.deleteObjective(1);
        verify(objectiveRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenObjectiveIdDoesNotExist_deleteObjective_ThrowsIllegalArgumentException() {
        when(objectiveRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> objectiveService.deleteObjective(999));
    }

    @Test
    public void whenDeleteObjectiveFails_deleteObjective_ThrowsException() {
        when(objectiveRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(objectiveRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> objectiveService.deleteObjective(1));
    }

    @Test
    public void whenObjectiveIdIsFound_updateObjective_UpdatesTheObjective() {
        ObjectiveDTO updateDTO = new ObjectiveDTO();
        updateDTO.setDescription("Updated description");

        when(objectiveRepository.findById(1)).thenReturn(Optional.of(entity));
        when(objectiveRepository.existsById(1)).thenReturn(true);
        when(objectiveRepository.save(entity)).thenReturn(entity);
        when(objectiveMapper.mapToObjectiveDto(entity)).thenReturn(updateDTO);

        Optional<ObjectiveDTO> result = objectiveService.updateObjective(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated description", result.get().getDescription());
    }

    @Test
    public void whenObjectiveIdIsNotFound_updateObjective_ReturnsEmptyOptional() {
        ObjectiveDTO updateDTO = new ObjectiveDTO();
        updateDTO.setDescription("Updated");

        when(objectiveRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> objectiveService.updateObjective(999, updateDTO));
    }

    @Test
    public void whenObjectiveDescriptionIsInvalid_updateObjective_ThrowsIllegalArgumentException() {
        ObjectiveDTO updateEmptyDescription = new ObjectiveDTO();
        updateEmptyDescription.setDescription("");

        ObjectiveDTO updateNullDescription = new ObjectiveDTO();
        updateNullDescription.setDescription(null);

        when(objectiveRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> objectiveService.updateObjective(1, updateEmptyDescription));
        assertThrows(IllegalArgumentException.class, () -> objectiveService.updateObjective(1, updateNullDescription));
    }

    @Test
    public void whenObjectiveNameAlreadyExists_updateObjective_ThrowsDataIntegrityViolationException() {
        when(objectiveRepository.objectiveExists(objectiveMapper.mapFromObjectiveDto(dto))).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> objectiveService.updateObjective(entity.getId(), dto));
    }
    
}
