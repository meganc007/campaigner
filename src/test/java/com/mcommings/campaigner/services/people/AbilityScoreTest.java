package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.people.AbilityScore;
import com.mcommings.campaigner.models.people.GenericMonster;
import com.mcommings.campaigner.models.people.NamedMonster;
import com.mcommings.campaigner.models.people.Person;
import com.mcommings.campaigner.repositories.people.IAbilityScoreRepository;
import com.mcommings.campaigner.repositories.people.IGenericMonsterRepository;
import com.mcommings.campaigner.repositories.people.INamedMonsterRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_ABILITY_SCORE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AbilityScoreTest {

    @Mock
    private IAbilityScoreRepository abilityScoreRepository;
    @Mock
    private IPersonRepository personRepository;
    @Mock
    private IGenericMonsterRepository genericMonsterRepository;
    @Mock
    private INamedMonsterRepository namedMonsterRepository;

    @InjectMocks
    private AbilityScoreService abilityScoreService;

    @Test
    public void whenThereAreAbilityScores_getAbilityScores_ReturnsAbilityScores() {
        UUID campaign = UUID.randomUUID();
        List<AbilityScore> abilityScores = new ArrayList<>();
        abilityScores.add(new AbilityScore(1, campaign, 1, 1, 1, 1, 1, 1));
        abilityScores.add(new AbilityScore(2, campaign, 2, 2, 2, 2, 2, 2));
        when(abilityScoreRepository.findAll()).thenReturn(abilityScores);

        List<AbilityScore> result = abilityScoreService.getAbilityScores();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(abilityScores, result);
    }

    @Test
    public void whenThereAreNoAbilityScores_getAbilityScores_ReturnsNothing() {
        List<AbilityScore> abilityScores = new ArrayList<>();
        when(abilityScoreRepository.findAll()).thenReturn(abilityScores);

        List<AbilityScore> result = abilityScoreService.getAbilityScores();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(abilityScores, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getAbilityScoresByCampaignUUID_ReturnsAbilityScores() {
        UUID campaign = UUID.randomUUID();
        List<AbilityScore> abilityScores = new ArrayList<>();
        abilityScores.add(new AbilityScore(1, campaign, 1, 1, 1, 1, 1, 1));
        abilityScores.add(new AbilityScore(2, campaign, 2, 2, 2, 2, 2, 2));
        when(abilityScoreRepository.findByfk_campaign_uuid(campaign)).thenReturn(abilityScores);

        List<AbilityScore> results = abilityScoreService.getAbilityScoresByCampaignUUID(campaign);

        Assertions.assertEquals(2, results.size());
        Assertions.assertEquals(abilityScores, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getAbilityScoresByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<AbilityScore> abilityScores = new ArrayList<>();
        when(abilityScoreRepository.findByfk_campaign_uuid(campaign)).thenReturn(abilityScores);

        List<AbilityScore> result = abilityScoreService.getAbilityScoresByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(abilityScores, result);
    }

    @Test
    public void whenAbilityScoreIsValid_saveAbilityScore_SavesTheAbilityScore() {
        AbilityScore abilityScore = new AbilityScore(1, UUID.randomUUID(), 1, 1, 1, 1, 1, 1);
        when(abilityScoreRepository.saveAndFlush(abilityScore)).thenReturn(abilityScore);

        assertDoesNotThrow(() -> abilityScoreService.saveAbilityScore(abilityScore));
        verify(abilityScoreRepository, times(1)).saveAndFlush(abilityScore);
    }

    @Test
    public void whenAbilityScoreEqualsZero_saveAbilityScore_ThrowsIllegalArgumentException() {
        AbilityScore abilityScore = new AbilityScore(1, UUID.randomUUID(), 0, 1, 0, 1, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> abilityScoreService.saveAbilityScore(abilityScore));
    }

    @Test
    public void whenAbilityScoreAlreadyExists_saveAbilityScore_ThrowsDataIntegrityViolationException() {
        AbilityScore abilityScore = new AbilityScore(1, UUID.randomUUID(), 20, 20, 20, 20, 20, 20);
        when(abilityScoreRepository.abilityScoreExists(abilityScore)).thenReturn(Optional.of(abilityScore));
        assertThrows(DataIntegrityViolationException.class, () -> abilityScoreService.saveAbilityScore(abilityScore));
    }

    @Test
    public void whenAbilityScoreIdExists_deleteAbilityScore_DeletesTheAbilityScore() {
        int abilityScoreId = 1;
        when(abilityScoreRepository.existsById(abilityScoreId)).thenReturn(true);
        assertDoesNotThrow(() -> abilityScoreService.deleteAbilityScore(abilityScoreId));
        verify(abilityScoreRepository, times(1)).deleteById(abilityScoreId);
    }

    @Test
    public void whenAbilityScoreIdDoesNotExist_deleteAbilityScore_ThrowsIllegalArgumentException() {
        int abilityScoreId = 9000;
        when(abilityScoreRepository.existsById(abilityScoreId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> abilityScoreService.deleteAbilityScore(abilityScoreId));
    }

    @Test
    public void whenAbilityScoreIdIsAForeignKey_deleteAbilityScore_ThrowsDataIntegrityViolationException() {
        int abilityScoreId = 1;
        Person person = new Person(1, "Jane", "Doe", 33, "The Nameless One",
                2, 2, abilityScoreId, true, false, "Personality", "Description", "Notes");
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(personRepository));
        List<Person> people = new ArrayList<>(Arrays.asList(person));

        GenericMonster genericMonster = new GenericMonster(1, "name", "desc", abilityScoreId, "traits", "notes");
        List<GenericMonster> genericMonsters = new ArrayList<>(Arrays.asList(genericMonster));

        NamedMonster namedMonster = new NamedMonster(1, "First Name", "Last Name", "Title", 1, 1, abilityScoreId, false, "description", "personality", "notes");
        List<NamedMonster> namedMonsters = new ArrayList<>(Arrays.asList(namedMonster));

        when(abilityScoreRepository.existsById(abilityScoreId)).thenReturn(true);
        when(personRepository.findByfk_ability_score(abilityScoreId)).thenReturn(people);
        when(genericMonsterRepository.findByfk_ability_score(abilityScoreId)).thenReturn(genericMonsters);
        when(namedMonsterRepository.findByfk_ability_score(abilityScoreId)).thenReturn(namedMonsters);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_ABILITY_SCORE.columnName, abilityScoreId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> abilityScoreService.deleteAbilityScore(abilityScoreId));
    }

    @Test
    public void whenAbilityScoreIdIsFound_updateAbilityScore_UpdatesTheAbilityScore() {
        UUID campaign = UUID.randomUUID();
        int abilityScoreId = 1;
        AbilityScore abilityScore = new AbilityScore(abilityScoreId, campaign, 10, 10, 10, 10, 10, 10);
        AbilityScore abilityScoreToUpdate = new AbilityScore(abilityScoreId, campaign, 20, 20, 20, 20, 20, 20);

        when(abilityScoreRepository.existsById(abilityScoreId)).thenReturn(true);
        when(abilityScoreRepository.findById(abilityScoreId)).thenReturn(Optional.of(abilityScore));

        abilityScoreService.updateAbilityScore(abilityScoreId, abilityScoreToUpdate);

        verify(abilityScoreRepository).findById(abilityScoreId);

        AbilityScore result = abilityScoreRepository.findById(abilityScoreId).get();
        Assertions.assertEquals(abilityScoreToUpdate.getStrength(), result.getStrength());
        Assertions.assertEquals(abilityScoreToUpdate.getDexterity(), result.getDexterity());
        Assertions.assertEquals(abilityScoreToUpdate.getConstitution(), result.getConstitution());
        Assertions.assertEquals(abilityScoreToUpdate.getIntelligence(), result.getIntelligence());
        Assertions.assertEquals(abilityScoreToUpdate.getWisdom(), result.getWisdom());
        Assertions.assertEquals(abilityScoreToUpdate.getCharisma(), result.getCharisma());
    }

    @Test
    public void whenAbilityScoreIdIsNotFound_updateAbilityScore_ThrowsIllegalArgumentException() {
        int abilityScoreId = 1;
        AbilityScore abilityScore = new AbilityScore(abilityScoreId, UUID.randomUUID(), 10, 10, 10, 10, 10, 10);

        when(abilityScoreRepository.existsById(abilityScoreId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> abilityScoreService.updateAbilityScore(abilityScoreId, abilityScore));
    }

    @Test
    public void whenSomeAbilityScoreFieldsChanged_updateAbilityScore_OnlyUpdatesChangedFields() {
        int abilityScoreId = 1;
        AbilityScore abilityScore = new AbilityScore(abilityScoreId, UUID.randomUUID(), 10, 10, 19, 10,
                10, 13);

        int newCon = 17;
        int newInt = -1;

        AbilityScore abilityScoreToUpdate = new AbilityScore();
        abilityScoreToUpdate.setConstitution(newCon);
        abilityScoreToUpdate.setIntelligence(newInt);

        when(abilityScoreRepository.existsById(abilityScoreId)).thenReturn(true);
        when(abilityScoreRepository.findById(abilityScoreId)).thenReturn(Optional.of(abilityScore));

        abilityScoreService.updateAbilityScore(abilityScoreId, abilityScoreToUpdate);

        verify(abilityScoreRepository).findById(abilityScoreId);

        AbilityScore result = abilityScoreRepository.findById(abilityScoreId).get();
        Assertions.assertEquals(abilityScore.getStrength(), result.getStrength());
        Assertions.assertEquals(abilityScore.getDexterity(), result.getDexterity());
        Assertions.assertEquals(newCon, result.getConstitution());
        Assertions.assertEquals(abilityScore.getIntelligence(), result.getIntelligence());
        Assertions.assertEquals(abilityScore.getWisdom(), result.getWisdom());
        Assertions.assertEquals(abilityScore.getCharisma(), result.getCharisma());
    }
}
