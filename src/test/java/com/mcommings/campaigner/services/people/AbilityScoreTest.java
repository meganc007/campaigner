package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.models.people.AbilityScore;
import com.mcommings.campaigner.repositories.people.IAbilityScoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AbilityScoreTest {
    
    @Mock
    private IAbilityScoreRepository abilityScoreRepository;
    
    @InjectMocks
    private AbilityScoreService abilityScoreService;
    
    @Test
    public void whenThereAreAbilityScores_getAbilityScores_ReturnsAbilityScores() {
        List<AbilityScore> abilityScores = new ArrayList<>();
        abilityScores.add(new AbilityScore(1, 1, 1, 1, 1, 1, 1));
        abilityScores.add(new AbilityScore(2, 2, 2, 2, 2, 2, 2));
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
    public void whenAbilityScoreIsValid_saveAbilityScore_SavesTheAbilityScore() {
        AbilityScore abilityScore = new AbilityScore(1, 1, 1, 1, 1, 1, 1);
        when(abilityScoreRepository.saveAndFlush(abilityScore)).thenReturn(abilityScore);

        assertDoesNotThrow(() -> abilityScoreService.saveAbilityScore(abilityScore));
        verify(abilityScoreRepository, times(1)).saveAndFlush(abilityScore);
    }

    @Test
    public void whenAbilityScoreEqualsZero_saveAbilityScore_ThrowsIllegalArgumentException() {
        AbilityScore abilityScore = new AbilityScore(1, 0, 1, 0, 1, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> abilityScoreService.saveAbilityScore(abilityScore));
    }

    @Test
    public void whenAbilityScoreAlreadyExists_saveAbilityScore_ThrowsDataIntegrityViolationException() {
        AbilityScore abilityScore = new AbilityScore(1, 20, 20, 20, 20, 20, 20);
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
    
//    TODO: when People is added, test fk lookup on delete
    
    @Test
    public void whenAbilityScoreIdIsFound_updateAbilityScore_UpdatesTheAbilityScore() {
        int abilityScoreId = 1;
        AbilityScore abilityScore = new AbilityScore(abilityScoreId, 10, 10, 10, 10, 10, 10);
        AbilityScore abilityScoreToUpdate = new AbilityScore(abilityScoreId, 20, 20, 20, 20, 20, 20);

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
        AbilityScore abilityScore = new AbilityScore(abilityScoreId, 10, 10, 10, 10, 10, 10);

        when(abilityScoreRepository.existsById(abilityScoreId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> abilityScoreService.updateAbilityScore(abilityScoreId, abilityScore));
    }
}
