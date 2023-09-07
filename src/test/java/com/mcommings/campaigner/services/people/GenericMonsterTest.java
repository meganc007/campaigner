package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.people.GenericMonster;
import com.mcommings.campaigner.models.people.NamedMonster;
import com.mcommings.campaigner.repositories.people.IAbilityScoreRepository;
import com.mcommings.campaigner.repositories.people.IGenericMonsterRepository;
import com.mcommings.campaigner.repositories.people.INamedMonsterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mcommings.campaigner.enums.ForeignKey.FK_GENERIC_MONSTER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GenericMonsterTest {

    @Mock
    private IGenericMonsterRepository genericMonsterRepository;
    @Mock
    private IAbilityScoreRepository abilityScoreRepository;
    @Mock
    private INamedMonsterRepository namedMonsterRepository;

    @InjectMocks
    private GenericMonsterService genericMonsterService;

    @Test
    public void whenThereAreGenericMonsters_getGenericMonsters_ReturnsGenericMonsters() {
        List<GenericMonster> genericMonsters = new ArrayList<>();
        genericMonsters.add(new GenericMonster(1, "GenericMonster 1", "Description 1"));
        genericMonsters.add(new GenericMonster(2, "GenericMonster 2", "Description 2"));
        genericMonsters.add(new GenericMonster(3, "GenericMonster 3", "Description 3", 1, "Traits", "Notes"));
        when(genericMonsterRepository.findAll()).thenReturn(genericMonsters);

        List<GenericMonster> result = genericMonsterService.getGenericMonsters();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(genericMonsters, result);
    }

    @Test
    public void whenThereAreNoGenericMonsters_getGenericMonsters_ReturnsNothing() {
        List<GenericMonster> genericMonsters = new ArrayList<>();
        when(genericMonsterRepository.findAll()).thenReturn(genericMonsters);

        List<GenericMonster> result = genericMonsterService.getGenericMonsters();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(genericMonsters, result);
    }

    @Test
    public void whenGenericMonsterWithNoForeignKeysIsValid_saveGenericMonster_SavesTheGenericMonster() {
        GenericMonster genericMonster = new GenericMonster(1, "GenericMonster 1", "Description 1");
        when(genericMonsterRepository.saveAndFlush(genericMonster)).thenReturn(genericMonster);

        assertDoesNotThrow(() -> genericMonsterService.saveGenericMonster(genericMonster));

        verify(genericMonsterRepository, times(1)).saveAndFlush(genericMonster);
    }

    @Test
    public void whenGenericMonsterWithForeignKeysIsValid_saveGenericMonster_SavesTheGenericMonster() {
        GenericMonster genericMonster = new GenericMonster(1, "GenericMonster", "Description", 1, "Traits", "Notes");

        when(abilityScoreRepository.existsById(1)).thenReturn(true);
        when(genericMonsterRepository.saveAndFlush(genericMonster)).thenReturn(genericMonster);

        assertDoesNotThrow(() -> genericMonsterService.saveGenericMonster(genericMonster));

        verify(genericMonsterRepository, times(1)).saveAndFlush(genericMonster);
    }

    @Test
    public void whenGenericMonsterNameIsInvalid_saveGenericMonster_ThrowsIllegalArgumentException() {
        GenericMonster genericMonsterWithEmptyName = new GenericMonster(1, "", "Description 1");
        GenericMonster genericMonsterWithNullName = new GenericMonster(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> genericMonsterService.saveGenericMonster(genericMonsterWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> genericMonsterService.saveGenericMonster(genericMonsterWithNullName));
    }

    @Test
    public void whenGenericMonsterNameAlreadyExists_saveGenericMonster_ThrowsDataIntegrityViolationException() {
        GenericMonster genericMonster = new GenericMonster(1, "GenericMonster", "Description", 1, "Traits", "Notes");
        GenericMonster genericMonsterCopy = new GenericMonster(2, "GenericMonster", "Description", 1, "Traits", "Notes");

        when(genericMonsterRepository.existsById(1)).thenReturn(true);
        when(abilityScoreRepository.existsById(1)).thenReturn(true);

        when(genericMonsterRepository.saveAndFlush(genericMonster)).thenReturn(genericMonster);
        when(genericMonsterRepository.saveAndFlush(genericMonsterCopy)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> genericMonsterService.saveGenericMonster(genericMonster));
        assertThrows(DataIntegrityViolationException.class, () -> genericMonsterService.saveGenericMonster(genericMonsterCopy));
    }

    @Test
    public void whenGenericMonsterHasInvalidForeignKeys_saveGenericMonster_ThrowsDataIntegrityViolationException() {
        GenericMonster genericMonster = new GenericMonster(1, "GenericMonster", "Description", 1, "Traits", "Notes");

        when(abilityScoreRepository.existsById(2)).thenReturn(false);
        when(genericMonsterRepository.saveAndFlush(genericMonster)).thenReturn(genericMonster);

        assertThrows(DataIntegrityViolationException.class, () -> genericMonsterService.saveGenericMonster(genericMonster));
    }

    @Test
    public void whenGenericMonsterIdExists_deleteGenericMonster_DeletesTheGenericMonster() {
        int genericMonsterId = 1;
        when(genericMonsterRepository.existsById(genericMonsterId)).thenReturn(true);
        assertDoesNotThrow(() -> genericMonsterService.deleteGenericMonster(genericMonsterId));
        verify(genericMonsterRepository, times(1)).deleteById(genericMonsterId);
    }

    @Test
    public void whenGenericMonsterIdDoesNotExist_deleteGenericMonster_ThrowsIllegalArgumentException() {
        int genericMonsterId = 9000;
        when(genericMonsterRepository.existsById(genericMonsterId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> genericMonsterService.deleteGenericMonster(genericMonsterId));
    }

    @Test
    public void whenGenericMonsterIdIsAForeignKey_deleteGenericMonster_ThrowsDataIntegrityViolationException() {
        int genericMonsterId = 1;
        NamedMonster namedMonster = new NamedMonster(1, "First Name", "Last Name", "Title",
                1, 2, genericMonsterId, false, "Personality", "Description", "Notes");
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(namedMonsterRepository));
        List<NamedMonster> namedMonsters = new ArrayList<>(Arrays.asList(namedMonster));

        when(genericMonsterRepository.existsById(genericMonsterId)).thenReturn(true);
        when(namedMonsterRepository.findByfk_generic_monster(genericMonsterId)).thenReturn(namedMonsters);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_GENERIC_MONSTER.columnName, genericMonsterId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> genericMonsterService.deleteGenericMonster(genericMonsterId));
    }

    @Test
    public void whenGenericMonsterIdWithNoFKIsFound_updateGenericMonster_UpdatesTheGenericMonster() {
        int genericMonsterId = 1;

        GenericMonster genericMonster = new GenericMonster(genericMonsterId, "Old GenericMonster Name", "Old Description");
        GenericMonster updateNoFK = new GenericMonster(genericMonsterId, "Updated GenericMonster Name", "Updated Description");

        when(genericMonsterRepository.existsById(genericMonsterId)).thenReturn(true);
        when(genericMonsterRepository.findById(genericMonsterId)).thenReturn(Optional.of(genericMonster));

        genericMonsterService.updateGenericMonster(genericMonsterId, updateNoFK);

        verify(genericMonsterRepository).findById(genericMonsterId);

        GenericMonster result1 = genericMonsterRepository.findById(genericMonsterId).get();
        Assertions.assertEquals(updateNoFK.getName(), result1.getName());
        Assertions.assertEquals(updateNoFK.getDescription(), result1.getDescription());
    }

    @Test
    public void whenGenericMonsterIdWithValidFKIsFound_updateGenericMonster_UpdatesTheGenericMonster() {
        int genericMonsterId = 2;

        GenericMonster genericMonster = new GenericMonster(1, "GenericMonster", "Description", 1, "Traits", "Notes");
        GenericMonster update = new GenericMonster(1, "GenericMonster", "Description", 3, "Traits", "Notes");

        when(genericMonsterRepository.existsById(genericMonsterId)).thenReturn(true);
        when(genericMonsterRepository.findById(genericMonsterId)).thenReturn(Optional.of(genericMonster));
        when(abilityScoreRepository.existsById(1)).thenReturn(true);
        when(abilityScoreRepository.existsById(3)).thenReturn(true);

        genericMonsterService.updateGenericMonster(genericMonsterId, update);

        verify(genericMonsterRepository).findById(genericMonsterId);

        GenericMonster result = genericMonsterRepository.findById(genericMonsterId).get();
        Assertions.assertEquals(update.getName(), result.getName());
        Assertions.assertEquals(update.getFk_ability_score(), result.getFk_ability_score());
        Assertions.assertEquals(update.getTraits(), result.getTraits());
        Assertions.assertEquals(update.getDescription(), result.getDescription());
        Assertions.assertEquals(update.getNotes(), result.getNotes());
    }

    @Test
    public void whenGenericMonsterIdWithInvalidFKIsFound_updateGenericMonster_ThrowsDataIntegrityViolationException() {
        int genericMonsterId = 2;

        GenericMonster genericMonster = new GenericMonster(1, "GenericMonster", "Description", 1, "Traits", "Notes");
        GenericMonster update = new GenericMonster(1, "GenericMonster", "Description", 3, "Traits", "Notes");

        when(genericMonsterRepository.existsById(genericMonsterId)).thenReturn(true);
        when(genericMonsterRepository.findById(genericMonsterId)).thenReturn(Optional.of(genericMonster));
        when(abilityScoreRepository.existsById(1)).thenReturn(true);
        when(abilityScoreRepository.existsById(3)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> genericMonsterService.updateGenericMonster(genericMonsterId, update));
    }

    @Test
    public void whenGenericMonsterIdIsNotFound_updateGenericMonster_ThrowsIllegalArgumentException() {
        int genericMonsterId = 1;
        GenericMonster genericMonster = new GenericMonster(genericMonsterId, "Old GenericMonster Name", "Old Description");

        when(genericMonsterRepository.existsById(genericMonsterId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> genericMonsterService.updateGenericMonster(genericMonsterId, genericMonster));
    }
}
