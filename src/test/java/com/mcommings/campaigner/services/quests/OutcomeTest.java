package com.mcommings.campaigner.services.quests;

import com.mcommings.campaigner.modules.quests.dtos.OutcomeDTO;
import com.mcommings.campaigner.modules.quests.entities.Outcome;
import com.mcommings.campaigner.modules.quests.mappers.OutcomeMapper;
import com.mcommings.campaigner.modules.quests.repositories.IOutcomeRepository;
import com.mcommings.campaigner.modules.quests.services.OutcomeService;
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
public class OutcomeTest {

    @Mock
    private OutcomeMapper outcomeMapper;

    @Mock
    private IOutcomeRepository outcomeRepository;

    @InjectMocks
    private OutcomeService outcomeService;

    private Outcome entity;
    private OutcomeDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Outcome();
        entity.setId(1);
        entity.setDescription("This is a description.");
        entity.setNotes("This is a note.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new OutcomeDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());

        when(outcomeMapper.mapToOutcomeDto(entity)).thenReturn(dto);
        when(outcomeMapper.mapFromOutcomeDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreOutcomes_getOutcomes_ReturnsOutcomes() {
        when(outcomeRepository.findAll()).thenReturn(List.of(entity));
        List<OutcomeDTO> result = outcomeService.getOutcomes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("This is a description.", result.get(0).getDescription());
    }

    @Test
    public void whenThereAreNoOutcomes_getOutcomes_ReturnsNothing() {
        when(outcomeRepository.findAll()).thenReturn(Collections.emptyList());

        List<OutcomeDTO> result = outcomeService.getOutcomes();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no outcomes.");
    }

    @Test
    public void whenThereIsAOutcome_getOutcome_ReturnsOutcome() {
        when(outcomeRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<OutcomeDTO> result = outcomeService.getOutcome(1);

        assertTrue(result.isPresent());
        assertEquals("This is a description.", result.get().getDescription());
    }

    @Test
    public void whenThereIsNotAOutcome_getOutcome_ReturnsOutcome() {
        when(outcomeRepository.findById(999)).thenReturn(Optional.empty());

        Optional<OutcomeDTO> result = outcomeService.getOutcome(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when outcome is not found.");
    }

    @Test
    public void whenCampaignUUIDIsValid_getOutcomesByCampaignUUID_ReturnsOutcomes() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(outcomeRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<OutcomeDTO> result = outcomeService.getOutcomesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getOutcomesByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(outcomeRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<OutcomeDTO> result = outcomeService.getOutcomesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no outcomes match the campaign UUID.");
    }

    @Test
    public void whenOutcomeIsValid_saveOutcome_SavesTheOutcome() {
        when(outcomeRepository.save(entity)).thenReturn(entity);

        outcomeService.saveOutcome(dto);

        verify(outcomeRepository, times(1)).save(entity);
    }

    @Test
    public void whenOutcomeDescriptionIsInvalid_saveOutcome_ThrowsIllegalArgumentException() {
        OutcomeDTO outcomeWithEmptyDescription = new OutcomeDTO();
        outcomeWithEmptyDescription.setId(1);
        outcomeWithEmptyDescription.setDescription("");
        outcomeWithEmptyDescription.setFk_campaign_uuid(UUID.randomUUID());

        OutcomeDTO outcomeWithNullDescription = new OutcomeDTO();
        outcomeWithNullDescription.setId(1);
        outcomeWithNullDescription.setDescription(null);
        outcomeWithNullDescription.setFk_campaign_uuid(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> outcomeService.saveOutcome(outcomeWithEmptyDescription));
        assertThrows(IllegalArgumentException.class, () -> outcomeService.saveOutcome(outcomeWithNullDescription));
    }

    @Test
    public void whenOutcomeDescriptionAlreadyExists_saveOutcome_ThrowsDataIntegrityViolationException() {
        when(outcomeRepository.outcomeExists(outcomeMapper.mapFromOutcomeDto(dto))).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> outcomeService.saveOutcome(dto));
        verify(outcomeRepository, times(1)).outcomeExists(outcomeMapper.mapFromOutcomeDto(dto));
        verify(outcomeRepository, never()).save(any(Outcome.class));
    }

    @Test
    public void whenOutcomeIdExists_deleteOutcome_DeletesTheOutcome() {
        when(outcomeRepository.existsById(1)).thenReturn(true);
        outcomeService.deleteOutcome(1);
        verify(outcomeRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenOutcomeIdDoesNotExist_deleteOutcome_ThrowsIllegalArgumentException() {
        when(outcomeRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> outcomeService.deleteOutcome(999));
    }

    @Test
    public void whenDeleteOutcomeFails_deleteOutcome_ThrowsException() {
        when(outcomeRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(outcomeRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> outcomeService.deleteOutcome(1));
    }

    @Test
    public void whenOutcomeIdIsFound_updateOutcome_UpdatesTheOutcome() {
        OutcomeDTO updateDTO = new OutcomeDTO();
        updateDTO.setDescription("Updated description");

        when(outcomeRepository.findById(1)).thenReturn(Optional.of(entity));
        when(outcomeRepository.existsById(1)).thenReturn(true);
        when(outcomeRepository.save(entity)).thenReturn(entity);
        when(outcomeMapper.mapToOutcomeDto(entity)).thenReturn(updateDTO);

        Optional<OutcomeDTO> result = outcomeService.updateOutcome(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated description", result.get().getDescription());
    }

    @Test
    public void whenOutcomeIdIsNotFound_updateOutcome_ReturnsEmptyOptional() {
        OutcomeDTO updateDTO = new OutcomeDTO();
        updateDTO.setDescription("Updated");

        when(outcomeRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> outcomeService.updateOutcome(999, updateDTO));
    }

    @Test
    public void whenOutcomeDescriptionIsInvalid_updateOutcome_ThrowsIllegalArgumentException() {
        OutcomeDTO updateEmptyDescription = new OutcomeDTO();
        updateEmptyDescription.setDescription("");

        OutcomeDTO updateNullDescription = new OutcomeDTO();
        updateNullDescription.setDescription(null);

        when(outcomeRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> outcomeService.updateOutcome(1, updateEmptyDescription));
        assertThrows(IllegalArgumentException.class, () -> outcomeService.updateOutcome(1, updateNullDescription));
    }

    @Test
    public void whenOutcomeNameAlreadyExists_updateOutcome_ThrowsDataIntegrityViolationException() {
        when(outcomeRepository.outcomeExists(outcomeMapper.mapFromOutcomeDto(dto))).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> outcomeService.updateOutcome(entity.getId(), dto));
    }
}
