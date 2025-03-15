package com.mcommings.campaigner.services.quests;

import com.mcommings.campaigner.modules.quests.entities.Objective;
import com.mcommings.campaigner.modules.quests.repositories.IObjectiveRepository;
import com.mcommings.campaigner.modules.quests.services.ObjectiveService;
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
public class ObjectiveTest {

    @Mock
    private IObjectiveRepository objectiveRepository;

    @InjectMocks
    private ObjectiveService objectiveService;

    @Test
    public void whenThereAreObjectives_getObjectives_ReturnsObjectives() {
        List<Objective> objectives = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        objectives.add(new Objective(1, "Objective 1", "Notes 1", campaign));
        objectives.add(new Objective(2, "Objective 2", "Notes 2", campaign));
        objectives.add(new Objective(2, "Objective 3", campaign));

        when(objectiveRepository.findAll()).thenReturn(objectives);

        List<Objective> result = objectiveService.getObjectives();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(objectives, result);
    }

    @Test
    public void whenThereAreNoObjectives_getObjectives_ReturnsNothing() {
        List<Objective> objectives = new ArrayList<>();
        when(objectiveRepository.findAll()).thenReturn(objectives);

        List<Objective> result = objectiveService.getObjectives();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(objectives, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getObjectivesByCampaignUUID_ReturnsObjectives() {
        List<Objective> objectives = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        objectives.add(new Objective(1, "Objective 1", "Notes 1", campaign));
        objectives.add(new Objective(2, "Objective 2", "Notes 2", campaign));
        objectives.add(new Objective(2, "Objective 3", campaign));
        when(objectiveRepository.findByfk_campaign_uuid(campaign)).thenReturn(objectives);

        List<Objective> results = objectiveService.getObjectivesByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(objectives, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getObjectivesByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Objective> objectives = new ArrayList<>();
        when(objectiveRepository.findByfk_campaign_uuid(campaign)).thenReturn(objectives);

        List<Objective> result = objectiveService.getObjectivesByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(objectives, result);
    }

    @Test
    public void whenObjectiveIsValid_saveObjective_SavesTheObjective() {
        Objective objective = new Objective(1, "Objective 1", "Notes 1", UUID.randomUUID());
        when(objectiveRepository.saveAndFlush(objective)).thenReturn(objective);

        assertDoesNotThrow(() -> objectiveService.saveObjective(objective));
        verify(objectiveRepository, times(1)).saveAndFlush(objective);
    }

    @Test
    public void whenObjectiveNameIsInvalid_saveObjective_ThrowsIllegalArgumentException() {
        Objective objectiveWithEmptyName = new Objective(1, "", "Notes 1", UUID.randomUUID());
        Objective objectiveWithNullName = new Objective(2, null, "Notes 2", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> objectiveService.saveObjective(objectiveWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> objectiveService.saveObjective(objectiveWithNullName));
    }

    @Test
    public void whenObjectiveDescriptionAlreadyExists_saveObjective_ThrowsDataIntegrityViolationException() {
        Objective objective = new Objective(1, "Objective 1", "Note 1", UUID.randomUUID());
        Objective objectiveWithDuplicatedName = new Objective(2, "Objective 1", "Note 2", UUID.randomUUID());
        when(objectiveRepository.saveAndFlush(objective)).thenReturn(objective);
        when(objectiveRepository.saveAndFlush(objectiveWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> objectiveService.saveObjective(objective));
        assertThrows(DataIntegrityViolationException.class, () -> objectiveService.saveObjective(objectiveWithDuplicatedName));
    }

    @Test
    public void whenObjectiveIdExists_deleteObjective_DeletesTheObjective() {
        int objectiveId = 1;
        when(objectiveRepository.existsById(objectiveId)).thenReturn(true);
        assertDoesNotThrow(() -> objectiveService.deleteObjective(objectiveId));
        verify(objectiveRepository, times(1)).deleteById(objectiveId);
    }

    @Test
    public void whenObjectiveIdDoesNotExist_deleteObjective_ThrowsIllegalArgumentException() {
        int objectiveId = 9000;
        when(objectiveRepository.existsById(objectiveId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> objectiveService.deleteObjective(objectiveId));
    }

    // TODO: fix when Objective is used as a fk
//    @Test
//    public void whenObjectiveIdIsAForeignKey_deleteObjective_ThrowsDataIntegrityViolationException() {
//        int objectiveId = 1;
//        Region region = new Region(1, "Region", "Description", 1, objectiveId);
//        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(regionRepository));
//        List<Region> regions = new ArrayList<>(Arrays.asList(region));
//
//        when(objectiveRepository.existsById(objectiveId)).thenReturn(true);
//        when(regionRepository.findByfk_objective(objectiveId)).thenReturn(regions);
//
//        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_OBJECTIVE.columnName, objectiveId);
//        Assertions.assertTrue(actual);
//        assertThrows(DataIntegrityViolationException.class, () -> objectiveService.deleteObjective(objectiveId));
//    }

    @Test
    public void whenObjectiveIdIsFound_updateObjective_UpdatesTheObjective() {
        int objectiveId = 1;
        UUID campaign = UUID.randomUUID();
        Objective objective = new Objective(objectiveId, "Old Objective Name", "Old Notes", campaign);
        Objective objectiveToUpdate = new Objective(objectiveId, "Updated Objective Name", "Updated Notes", campaign);

        when(objectiveRepository.existsById(objectiveId)).thenReturn(true);
        when(objectiveRepository.findById(objectiveId)).thenReturn(Optional.of(objective));

        objectiveService.updateObjective(objectiveId, objectiveToUpdate);

        verify(objectiveRepository).findById(objectiveId);

        Objective result = objectiveRepository.findById(objectiveId).get();
        Assertions.assertEquals(objectiveToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(objectiveToUpdate.getNotes(), result.getNotes());
    }

    @Test
    public void whenObjectiveIdIsNotFound_updateObjective_ThrowsIllegalArgumentException() {
        int objectiveId = 1;
        Objective objective = new Objective(objectiveId, "Old Objective Name", "Old Description", UUID.randomUUID());

        when(objectiveRepository.existsById(objectiveId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> objectiveService.updateObjective(objectiveId, objective));
    }

    @Test
    public void whenSomeObjectiveFieldsChanged_updateObjective_OnlyUpdatesChangedFields() {
        int objectiveId = 1;
        Objective objective = new Objective(objectiveId, "Name", "Description", UUID.randomUUID());

        String newDescription = "New Objective description";

        Objective objectiveToUpdate = new Objective();
        objectiveToUpdate.setDescription(newDescription);

        when(objectiveRepository.existsById(objectiveId)).thenReturn(true);
        when(objectiveRepository.findById(objectiveId)).thenReturn(Optional.of(objective));

        objectiveService.updateObjective(objectiveId, objectiveToUpdate);

        verify(objectiveRepository).findById(objectiveId);

        Objective result = objectiveRepository.findById(objectiveId).get();
        Assertions.assertEquals(newDescription, result.getDescription());
        Assertions.assertEquals(objective.getNotes(), result.getNotes());
    }
}
