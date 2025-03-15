package com.mcommings.campaigner.services.quests;

import com.mcommings.campaigner.modules.quests.entities.Outcome;
import com.mcommings.campaigner.modules.quests.repositories.IOutcomeRepository;
import com.mcommings.campaigner.modules.quests.services.OutcomeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OutcomeTest {

    @Mock
    private IOutcomeRepository outcomeRepository;

    @InjectMocks
    private OutcomeService outcomeService;

    @Test
    public void whenThereAreOutcomes_getOutcomes_ReturnsOutcomes() {
        List<Outcome> outcomes = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        outcomes.add(new Outcome(1, "Outcome 1", "Notes 1", campaign));
        outcomes.add(new Outcome(2, "Outcome 2", "Notes 2", campaign));
        outcomes.add(new Outcome(2, "Outcome 3", campaign));

        when(outcomeRepository.findAll()).thenReturn(outcomes);

        List<Outcome> result = outcomeService.getOutcomes();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(outcomes, result);
    }

    @Test
    public void whenThereAreNoOutcomes_getOutcomes_ReturnsNothing() {
        List<Outcome> outcomes = new ArrayList<>();
        when(outcomeRepository.findAll()).thenReturn(outcomes);

        List<Outcome> result = outcomeService.getOutcomes();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(outcomes, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getOutcomesByCampaignUUID_ReturnsOutcomes() {
        List<Outcome> outcomes = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        outcomes.add(new Outcome(1, "Outcome 1", "Notes 1", campaign));
        outcomes.add(new Outcome(2, "Outcome 2", "Notes 2", campaign));
        outcomes.add(new Outcome(2, "Outcome 3", campaign));

        when(outcomeRepository.findByfk_campaign_uuid(campaign)).thenReturn(outcomes);

        List<Outcome> results = outcomeService.getOutcomesByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(outcomes, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getOutcomesByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Outcome> outcomes = new ArrayList<>();
        when(outcomeRepository.findByfk_campaign_uuid(campaign)).thenReturn(outcomes);

        List<Outcome> result = outcomeService.getOutcomesByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(outcomes, result);
    }

    @Test
    public void whenOutcomeIsValid_saveOutcome_SavesTheOutcome() {
        Outcome outcome = new Outcome(1, "Outcome 1", "Notes 1", UUID.randomUUID());
        when(outcomeRepository.saveAndFlush(outcome)).thenReturn(outcome);

        assertDoesNotThrow(() -> outcomeService.saveOutcome(outcome));
        verify(outcomeRepository, times(1)).saveAndFlush(outcome);
    }

    @Test
    public void whenOutcomeNameIsInvalid_saveOutcome_ThrowsIllegalArgumentException() {
        Outcome outcomeWithEmptyName = new Outcome(1, "", "Notes 1", UUID.randomUUID());
        Outcome outcomeWithNullName = new Outcome(2, null, "Notes 2", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> outcomeService.saveOutcome(outcomeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> outcomeService.saveOutcome(outcomeWithNullName));
    }

    @Test
    public void whenOutcomeDescriptionAlreadyExists_saveOutcome_ThrowsDataIntegrityViolationException() {
        Outcome outcome = new Outcome(1, "Outcome 1", "Note 1", UUID.randomUUID());
        Outcome outcomeWithDuplicatedName = new Outcome(2, "Outcome 1", "Note 2", UUID.randomUUID());
        when(outcomeRepository.saveAndFlush(outcome)).thenReturn(outcome);
        when(outcomeRepository.saveAndFlush(outcomeWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> outcomeService.saveOutcome(outcome));
        assertThrows(DataIntegrityViolationException.class, () -> outcomeService.saveOutcome(outcomeWithDuplicatedName));
    }

    @Test
    public void whenOutcomeIdExists_deleteOutcome_DeletesTheOutcome() {
        int outcomeId = 1;
        when(outcomeRepository.existsById(outcomeId)).thenReturn(true);
        assertDoesNotThrow(() -> outcomeService.deleteOutcome(outcomeId));
        verify(outcomeRepository, times(1)).deleteById(outcomeId);
    }

    @Test
    public void whenOutcomeIdDoesNotExist_deleteOutcome_ThrowsIllegalArgumentException() {
        int outcomeId = 9000;
        when(outcomeRepository.existsById(outcomeId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> outcomeService.deleteOutcome(outcomeId));
    }

    // TODO: fix when Outcome is used as a fk
//    @Test
//    public void whenOutcomeIdIsAForeignKey_deleteOutcome_ThrowsDataIntegrityViolationException() {
//        int outcomeId = 1;
//        Region region = new Region(1, "Region", "Description", 1, outcomeId);
//        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(regionRepository));
//        List<Region> regions = new ArrayList<>(Arrays.asList(region));
//
//        when(outcomeRepository.existsById(outcomeId)).thenReturn(true);
//        when(regionRepository.findByfk_outcome(outcomeId)).thenReturn(regions);
//
//        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_OUTCOME.columnName, outcomeId);
//        Assertions.assertTrue(actual);
//        assertThrows(DataIntegrityViolationException.class, () -> outcomeService.deleteOutcome(outcomeId));
//    }

    @Test
    public void whenOutcomeIdIsFound_updateOutcome_UpdatesTheOutcome() {
        int outcomeId = 1;
        UUID campaign = UUID.randomUUID();
        Outcome outcome = new Outcome(outcomeId, "Old Outcome Name", "Old Notes", campaign);
        Outcome outcomeToUpdate = new Outcome(outcomeId, "Updated Outcome Name", "Updated Notes", campaign);

        when(outcomeRepository.existsById(outcomeId)).thenReturn(true);
        when(outcomeRepository.findById(outcomeId)).thenReturn(Optional.of(outcome));

        outcomeService.updateOutcome(outcomeId, outcomeToUpdate);

        verify(outcomeRepository).findById(outcomeId);

        Outcome result = outcomeRepository.findById(outcomeId).get();
        Assertions.assertEquals(outcomeToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(outcomeToUpdate.getNotes(), result.getNotes());
    }

    @Test
    public void whenOutcomeIdIsNotFound_updateOutcome_ThrowsIllegalArgumentException() {
        int outcomeId = 1;
        Outcome outcome = new Outcome(outcomeId, "Old Outcome Name", "Old Description", UUID.randomUUID());

        when(outcomeRepository.existsById(outcomeId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> outcomeService.updateOutcome(outcomeId, outcome));
    }

    @Test
    public void whenSomeOutcomeFieldsChanged_updateOutcome_OnlyUpdatesChangedFields() {
        int outcomeId = 1;
        Outcome outcome = new Outcome(outcomeId, "Name", "Description", UUID.randomUUID());

        String newDescription = "New Outcome description";

        Outcome outcomeToUpdate = new Outcome();
        outcomeToUpdate.setDescription(newDescription);

        when(outcomeRepository.existsById(outcomeId)).thenReturn(true);
        when(outcomeRepository.findById(outcomeId)).thenReturn(Optional.of(outcome));

        outcomeService.updateOutcome(outcomeId, outcomeToUpdate);

        verify(outcomeRepository).findById(outcomeId);

        Outcome result = outcomeRepository.findById(outcomeId).get();
        Assertions.assertEquals(newDescription, result.getDescription());
        Assertions.assertEquals(outcome.getNotes(), result.getNotes());
    }
}
