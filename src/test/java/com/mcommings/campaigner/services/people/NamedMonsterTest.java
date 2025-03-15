package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.common.repositories.IWealthRepository;
import com.mcommings.campaigner.people.entities.NamedMonster;
import com.mcommings.campaigner.people.repositories.IAbilityScoreRepository;
import com.mcommings.campaigner.people.repositories.IGenericMonsterRepository;
import com.mcommings.campaigner.people.repositories.INamedMonsterRepository;
import com.mcommings.campaigner.people.services.NamedMonsterService;
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
public class NamedMonsterTest {

    @Mock
    private INamedMonsterRepository namedMonsterRepository;
    @Mock
    private IWealthRepository wealthRepository;
    @Mock
    private IAbilityScoreRepository abilityScoreRepository;
    @Mock
    private IGenericMonsterRepository genericMonsterRepository;

    @InjectMocks
    private NamedMonsterService namedMonsterService;

    @Test
    public void whenThereAreNamedMonsters_getNamedMonsters_ReturnsNamedMonsters() {
        List<NamedMonster> namedMonsters = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        namedMonsters.add(new NamedMonster(1, "First Name 1", "Last Name 1", "Title", false, "Personality", "Description", "Notes", campaign));
        namedMonsters.add(new NamedMonster(2, "First Name 2", "Last Name 2", "Title", false, "Personality", "Description", "Notes", campaign));
        namedMonsters.add(new NamedMonster(3, "First Name 3", "Last Name 3", "Title", 2, 4, 1, false, "Personality", "Description", "Notes", campaign));
        when(namedMonsterRepository.findAll()).thenReturn(namedMonsters);

        List<NamedMonster> result = namedMonsterService.getNamedMonsters();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(namedMonsters, result);
    }

    @Test
    public void whenThereAreNoNamedMonsters_getNamedMonsters_ReturnsNothing() {
        List<NamedMonster> namedMonsters = new ArrayList<>();
        when(namedMonsterRepository.findAll()).thenReturn(namedMonsters);

        List<NamedMonster> result = namedMonsterService.getNamedMonsters();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(namedMonsters, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getNamedMonstersByCampaignUUID_ReturnsNamedMonsters() {
        List<NamedMonster> namedMonsters = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        namedMonsters.add(new NamedMonster(1, "First Name 1", "Last Name 1", "Title", false, "Personality", "Description", "Notes", campaign));
        namedMonsters.add(new NamedMonster(2, "First Name 2", "Last Name 2", "Title", false, "Personality", "Description", "Notes", campaign));
        namedMonsters.add(new NamedMonster(3, "First Name 3", "Last Name 3", "Title", 2, 4, 1, false, "Personality", "Description", "Notes", campaign));
        when(namedMonsterRepository.findByfk_campaign_uuid(campaign)).thenReturn(namedMonsters);

        List<NamedMonster> results = namedMonsterService.getNamedMonstersByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(namedMonsters, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getNamedMonstersByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<NamedMonster> namedMonsters = new ArrayList<>();
        when(namedMonsterRepository.findByfk_campaign_uuid(campaign)).thenReturn(namedMonsters);

        List<NamedMonster> result = namedMonsterService.getNamedMonstersByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(namedMonsters, result);
    }

    @Test
    public void whenNamedMonsterWithNoForeignKeysIsValid_saveNamedMonster_SavesTheNamedMonster() {
        NamedMonster namedMonster = new NamedMonster(1, "First Name", "Last Name", "Title", true, "Personality", "Description", "Notes", UUID.randomUUID());
        when(namedMonsterRepository.saveAndFlush(namedMonster)).thenReturn(namedMonster);

        assertDoesNotThrow(() -> namedMonsterService.saveNamedMonster(namedMonster));

        verify(namedMonsterRepository, times(1)).saveAndFlush(namedMonster);
    }

    @Test
    public void whenNamedMonsterWithForeignKeysIsValid_saveNamedMonster_SavesTheNamedMonster() {
        NamedMonster namedMonster = new NamedMonster(1, "First Name", "Last Name", "Title",
                4, 2, 3, true, "Personality", "Description", "Notes", UUID.randomUUID());

        when(wealthRepository.existsById(4)).thenReturn(true);
        when(abilityScoreRepository.existsById(2)).thenReturn(true);
        when(genericMonsterRepository.existsById(3)).thenReturn(true);
        when(namedMonsterRepository.saveAndFlush(namedMonster)).thenReturn(namedMonster);

        assertDoesNotThrow(() -> namedMonsterService.saveNamedMonster(namedMonster));

        verify(namedMonsterRepository, times(1)).saveAndFlush(namedMonster);
    }

    @Test
    public void whenNamedMonsterNamesInvalid_saveNamedMonster_ThrowsIllegalArgumentException() {
        NamedMonster namedMonsterWithEmptyFirstName = new NamedMonster(1, "", "Last Name", "Title",
                4, 2, 3, true, "Personality", "Description", "Notes", UUID.randomUUID());
        NamedMonster namedMonsterWithEmptyLastName = new NamedMonster(1, "First Name", "", "Title",
                4, 2, 3, true, "Personality", "Description", "Notes", UUID.randomUUID());
        NamedMonster namedMonsterWithNullFirstName = new NamedMonster(1, null, "Last Name", "Title",
                4, 2, 3, true, "Personality", "Description", "Notes", UUID.randomUUID());
        NamedMonster namedMonsterWithNullLastName = new NamedMonster(1, "First Name", null, "Title",
                4, 2, 3, true, "Personality", "Description", "Notes", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.saveNamedMonster(namedMonsterWithEmptyFirstName));
        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.saveNamedMonster(namedMonsterWithEmptyLastName));
        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.saveNamedMonster(namedMonsterWithNullFirstName));
        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.saveNamedMonster(namedMonsterWithNullLastName));
    }

    @Test
    public void whenNamedMonsterAlreadyExists_saveNamedMonster_ThrowsDataIntegrityViolationException() {
        NamedMonster namedMonster = new NamedMonster(1, "First Name", "Last Name", "Title",
                4, 2, 3, true, "Personality", "Description", "Notes", UUID.randomUUID());
        NamedMonster doppelganger = new NamedMonster(2, "First Name", "Last Name", "Title",
                4, 2, 3, true, "Personality", "Description", "Notes", UUID.randomUUID());

        when(namedMonsterRepository.existsById(1)).thenReturn(true);
        when(wealthRepository.existsById(4)).thenReturn(true);
        when(abilityScoreRepository.existsById(2)).thenReturn(true);
        when(genericMonsterRepository.existsById(3)).thenReturn(true);

        when(namedMonsterRepository.saveAndFlush(namedMonster)).thenReturn(namedMonster);
        when(namedMonsterRepository.saveAndFlush(doppelganger)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> namedMonsterService.saveNamedMonster(namedMonster));
        assertThrows(DataIntegrityViolationException.class, () -> namedMonsterService.saveNamedMonster(doppelganger));
    }

    @Test
    public void whenNamedMonsterIdExists_deleteNamedMonster_DeletesTheNamedMonster() {
        int namedMonsterId = 1;
        when(namedMonsterRepository.existsById(namedMonsterId)).thenReturn(true);
        assertDoesNotThrow(() -> namedMonsterService.deleteNamedMonster(namedMonsterId));
        verify(namedMonsterRepository, times(1)).deleteById(namedMonsterId);
    }

    @Test
    public void whenNamedMonsterIdDoesNotExist_deleteNamedMonster_ThrowsIllegalArgumentException() {
        int namedMonsterId = 9000;
        when(namedMonsterRepository.existsById(namedMonsterId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.deleteNamedMonster(namedMonsterId));
    }

//    TODO: uncomment/fix when tables that use NamedMonster as fk added
//    @Test
//    public void whenNamedMonsterIdIsAForeignKey_deleteNamedMonster_ThrowsDataIntegrityViolationException() {
//        int namedMonsterId = 1;
//        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(jobAssignmentRepository));
//
//        JobAssignment jobAssignment = new JobAssignment(1, namedMonsterId, 1);
//        List<JobAssignment> jobAssignments = new ArrayList<>(Arrays.asList(jobAssignment));
//
//        EventPlaceNamedMonster epp = new EventPlaceNamedMonster(1, 1, 1, namedMonsterId);
//        List<EventPlaceNamedMonster> epps = new ArrayList<>(Arrays.asList(epp));
//
//
//        when(namedMonsterRepository.existsById(namedMonsterId)).thenReturn(true);
//        when(jobAssignmentRepository.findByfk_namedMonster(namedMonsterId)).thenReturn(jobAssignments);
//        when(eventPlaceNamedMonsterRepository.findByfk_namedMonster(namedMonsterId)).thenReturn(epps);
//
//        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_NAMEDMONSTER.columnName, namedMonsterId);
//        Assertions.assertTrue(actual);
//        assertThrows(DataIntegrityViolationException.class, () -> namedMonsterService.deleteNamedMonster(namedMonsterId));
//    }

    @Test
    public void whenNamedMonsterIdWithNoFKIsFound_updateNamedMonster_UpdatesTheNamedMonster() {
        int namedMonsterId = 1;
        UUID campaign = UUID.randomUUID();

        NamedMonster namedMonster = new NamedMonster(namedMonsterId, "First Name", "Last Name", "Title",
                false, "Personality", "Description", "Notes", campaign);
        NamedMonster namedMonsterToUpdate = new NamedMonster(namedMonsterId, "Jane", "Doe", "The Nameless",
                true, "Personality", "Description", "Notes", campaign);

        when(namedMonsterRepository.existsById(namedMonsterId)).thenReturn(true);
        when(namedMonsterRepository.findById(namedMonsterId)).thenReturn(Optional.of(namedMonster));

        namedMonsterService.updateNamedMonster(namedMonsterId, namedMonsterToUpdate);

        verify(namedMonsterRepository).findById(namedMonsterId);

        NamedMonster result = namedMonsterRepository.findById(namedMonsterId).get();
        Assertions.assertEquals(namedMonsterToUpdate.getFirstName(), result.getFirstName());
        Assertions.assertEquals(namedMonsterToUpdate.getLastName(), result.getLastName());
        Assertions.assertEquals(namedMonsterToUpdate.getTitle(), result.getTitle());
        Assertions.assertEquals(namedMonsterToUpdate.getIsEnemy(), result.getIsEnemy());
        Assertions.assertEquals(namedMonsterToUpdate.getPersonality(), result.getPersonality());
        Assertions.assertEquals(namedMonsterToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(namedMonsterToUpdate.getNotes(), result.getNotes());
    }

    @Test
    public void whenNamedMonsterIdWithValidFKIsFound_updateNamedMonster_UpdatesTheNamedMonster() {
        int namedMonsterId = 1;
        UUID campaign = UUID.randomUUID();

        NamedMonster namedMonster = new NamedMonster(namedMonsterId, "First Name", "Last Name", "Title",
                1, 2, 3, false, "Personality", "Description", "Notes", campaign);
        NamedMonster namedMonsterToUpdate = new NamedMonster(namedMonsterId, "Jane", "Doe", "The Nameless",
                4, 5, 6, true, "Personality", "Description", "Notes", campaign);

        when(namedMonsterRepository.existsById(namedMonsterId)).thenReturn(true);
        when(namedMonsterRepository.findById(namedMonsterId)).thenReturn(Optional.of(namedMonster));
        when(wealthRepository.existsById(1)).thenReturn(true);
        when(abilityScoreRepository.existsById(2)).thenReturn(true);
        when(genericMonsterRepository.existsById(3)).thenReturn(true);

        when(wealthRepository.existsById(4)).thenReturn(true);
        when(abilityScoreRepository.existsById(5)).thenReturn(true);
        when(genericMonsterRepository.existsById(6)).thenReturn(true);

        namedMonsterService.updateNamedMonster(namedMonsterId, namedMonsterToUpdate);

        verify(namedMonsterRepository).findById(namedMonsterId);

        NamedMonster result = namedMonsterRepository.findById(namedMonsterId).get();
        Assertions.assertEquals(namedMonsterToUpdate.getFirstName(), result.getFirstName());
        Assertions.assertEquals(namedMonsterToUpdate.getLastName(), result.getLastName());
        Assertions.assertEquals(namedMonsterToUpdate.getTitle(), result.getTitle());
        Assertions.assertEquals(namedMonsterToUpdate.getFk_wealth(), result.getFk_wealth());
        Assertions.assertEquals(namedMonsterToUpdate.getFk_ability_score(), result.getFk_ability_score());
        Assertions.assertEquals(namedMonsterToUpdate.getFk_generic_monster(), result.getFk_generic_monster());
        Assertions.assertEquals(namedMonsterToUpdate.getIsEnemy(), result.getIsEnemy());
        Assertions.assertEquals(namedMonsterToUpdate.getPersonality(), result.getPersonality());
        Assertions.assertEquals(namedMonsterToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(namedMonsterToUpdate.getNotes(), result.getNotes());
    }

    @Test
    public void whenNamedMonsterIdWithInvalidFKIsFound_updateNamedMonster_ThrowsDataIntegrityViolationException() {
        int namedMonsterId = 1;
        UUID campaign = UUID.randomUUID();

        NamedMonster namedMonster = new NamedMonster(namedMonsterId, "First Name", "Last Name", "Title",
                1, 2, 3, false, "Personality", "Description", "Notes", campaign);
        NamedMonster namedMonsterToUpdate = new NamedMonster(namedMonsterId, "Jane", "Doe", "The Nameless",
                4, 5, 6, true, "Personality", "Description", "Notes", campaign);

        when(namedMonsterRepository.existsById(namedMonsterId)).thenReturn(true);
        when(namedMonsterRepository.findById(namedMonsterId)).thenReturn(Optional.of(namedMonster));
        when(wealthRepository.existsById(1)).thenReturn(true);
        when(abilityScoreRepository.existsById(2)).thenReturn(false);
        when(genericMonsterRepository.existsById(3)).thenReturn(true);

        when(wealthRepository.existsById(4)).thenReturn(false);
        when(abilityScoreRepository.existsById(5)).thenReturn(true);
        when(genericMonsterRepository.existsById(6)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> namedMonsterService.updateNamedMonster(namedMonsterId, namedMonsterToUpdate));
    }

    @Test
    public void whenNamedMonsterIdIsNotFound_updateNamedMonster_ThrowsIllegalArgumentException() {
        int namedMonsterId = 1;
        NamedMonster namedMonster = new NamedMonster(namedMonsterId, "First Name", "Last Name", "Title",
                1, 2, 3, false, "Personality", "Description", "Notes", UUID.randomUUID());


        when(namedMonsterRepository.existsById(namedMonsterId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.updateNamedMonster(namedMonsterId, namedMonster));
    }

    @Test
    public void whenSomeNamedMonsterFieldsChanged_updateNamedMonster_OnlyUpdatesChangedFields() {
        int namedMonsterId = 1;
        NamedMonster namedMonster = new NamedMonster(namedMonsterId, "First Name", "Last Name", "Title",
                1, 2, 3, false, "Personality", "Description", "Notes", UUID.randomUUID());

        String newDescription = "New NamedMonster description";
        int newAbilityScore = 3;

        NamedMonster namedMonsterToUpdate = new NamedMonster();
        namedMonsterToUpdate.setDescription(newDescription);
        namedMonsterToUpdate.setFk_ability_score(newAbilityScore);

        when(namedMonsterRepository.existsById(namedMonsterId)).thenReturn(true);
        when(wealthRepository.existsById(1)).thenReturn(true);
        when(abilityScoreRepository.existsById(newAbilityScore)).thenReturn(true);
        when(genericMonsterRepository.existsById(3)).thenReturn(true);
        when(namedMonsterRepository.findById(namedMonsterId)).thenReturn(Optional.of(namedMonster));

        namedMonsterService.updateNamedMonster(namedMonsterId, namedMonsterToUpdate);

        verify(namedMonsterRepository).findById(namedMonsterId);

        NamedMonster result = namedMonsterRepository.findById(namedMonsterId).get();
        Assertions.assertEquals(namedMonster.getFirstName(), result.getFirstName());
        Assertions.assertEquals(namedMonster.getLastName(), result.getLastName());
        Assertions.assertEquals(namedMonster.getTitle(), result.getTitle());
        Assertions.assertEquals(namedMonster.getFk_wealth(), result.getFk_wealth());
        Assertions.assertEquals(newAbilityScore, result.getFk_ability_score());
        Assertions.assertEquals(namedMonster.getFk_generic_monster(), result.getFk_generic_monster());
        Assertions.assertEquals(namedMonster.getIsEnemy(), result.getIsEnemy());
        Assertions.assertEquals(namedMonster.getPersonality(), result.getPersonality());
        Assertions.assertEquals(newDescription, result.getDescription());
        Assertions.assertEquals(namedMonster.getNotes(), result.getNotes());
    }
}
