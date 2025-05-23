package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.modules.people.dtos.AbilityScoreDTO;
import com.mcommings.campaigner.modules.people.entities.AbilityScore;
import com.mcommings.campaigner.modules.people.mappers.AbilityScoreMapper;
import com.mcommings.campaigner.modules.people.repositories.IAbilityScoreRepository;
import com.mcommings.campaigner.modules.people.services.AbilityScoreService;
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
public class AbilityScoreTest {

    @Mock
    private AbilityScoreMapper abilityScoreMapper;
    
    @Mock
    private IAbilityScoreRepository abilityScoreRepository;

    @InjectMocks
    private AbilityScoreService abilityScoreService;

    private AbilityScore entity;
    private AbilityScoreDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new AbilityScore();
        entity.setId(1);
        entity.setStrength(random.nextInt(30) + 1);
        entity.setDexterity(random.nextInt(30) + 1);
        entity.setConstitution(random.nextInt(30) + 1);
        entity.setIntelligence(random.nextInt(30) + 1);
        entity.setWisdom(random.nextInt(30) + 1);
        entity.setCharisma(random.nextInt(30) + 1);
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new AbilityScoreDTO();
        dto.setId(entity.getId());
        dto.setStrength(entity.getStrength());
        dto.setDexterity(entity.getDexterity());
        dto.setConstitution(entity.getConstitution());
        dto.setIntelligence(entity.getIntelligence());
        dto.setWisdom(entity.getWisdom());
        dto.setCharisma(entity.getCharisma());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());

        when(abilityScoreMapper.mapToAbilityScoreDto(entity)).thenReturn(dto);
        when(abilityScoreMapper.mapFromAbilityScoreDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreAbilityScores_getAbilityScores_ReturnsAbilityScores() {
        when(abilityScoreRepository.findAll()).thenReturn(List.of(entity));
        List<AbilityScoreDTO> result = abilityScoreService.getAbilityScores();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    public void whenThereAreNoAbilityScores_getAbilityScores_ReturnsNothing() {
        when(abilityScoreRepository.findAll()).thenReturn(Collections.emptyList());

        List<AbilityScoreDTO> result = abilityScoreService.getAbilityScores();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no abilityScores.");
    }

    @Test
    public void whenThereIsAnAbilityScore_getAbilityScore_ReturnsAbilityScore() {
        when(abilityScoreRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<AbilityScoreDTO> result = abilityScoreService.getAbilityScore(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void whenThereIsNotAnAbilityScore_getAbilityScore_ReturnsAbilityScore() {
        when(abilityScoreRepository.findById(999)).thenReturn(Optional.empty());

        Optional<AbilityScoreDTO> result = abilityScoreService.getAbilityScore(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when abilityScore is not found.");
    }

    @Test
    public void whenCampaignUUIDIsValid_getAbilityScoresByCampaignUUID_ReturnsAbilityScores() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(abilityScoreRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<AbilityScoreDTO> result = abilityScoreService.getAbilityScoresByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getAbilityScoresByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(abilityScoreRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<AbilityScoreDTO> result = abilityScoreService.getAbilityScoresByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no abilityScores match the campaign UUID.");
    }

    @Test
    public void whenAbilityScoreIsValid_saveAbilityScore_SavesTheAbilityScore() {
        when(abilityScoreRepository.save(entity)).thenReturn(entity);

        abilityScoreService.saveAbilityScore(dto);

        verify(abilityScoreRepository, times(1)).save(entity);
    }

    @Test
    public void whenAbilityScoreIsInvalid_saveAbilityScore_ThrowsIllegalArgumentException() {
        AbilityScoreDTO abilityScore = new AbilityScoreDTO();
        abilityScore.setId(1);
        abilityScore.setFk_campaign_uuid(UUID.randomUUID());
        abilityScore.setStrength(0);
        abilityScore.setDexterity(1);
        abilityScore.setConstitution(1);
        abilityScore.setIntelligence(0);
        abilityScore.setWisdom(1);
        abilityScore.setCharisma(1);

        assertThrows(IllegalArgumentException.class, () -> abilityScoreService.saveAbilityScore(abilityScore));
    }

    @Test
    public void whenAbilityScoreNameAlreadyExists_saveAbilityScore_ThrowsDataIntegrityViolationException() {
        when(abilityScoreRepository.abilityScoreExists(any(AbilityScore.class))).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> abilityScoreService.saveAbilityScore(dto));
        verify(abilityScoreRepository, times(1)).abilityScoreExists(any(AbilityScore.class));
        verify(abilityScoreRepository, never()).save(any(AbilityScore.class));
    }

    @Test
    public void whenAbilityScoreIdExists_deleteAbilityScore_DeletesTheAbilityScore() {
        when(abilityScoreRepository.existsById(1)).thenReturn(true);
        abilityScoreService.deleteAbilityScore(1);
        verify(abilityScoreRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenAbilityScoreIdDoesNotExist_deleteAbilityScore_ThrowsIllegalArgumentException() {
        when(abilityScoreRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> abilityScoreService.deleteAbilityScore(999));
    }

    @Test
    public void whenDeleteAbilityScoreFails_deleteAbilityScore_ThrowsException() {
        when(abilityScoreRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(abilityScoreRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> abilityScoreService.deleteAbilityScore(1));
    }

    @Test
    public void whenAbilityScoreIdIsFound_updateAbilityScore_UpdatesTheAbilityScore() {
        AbilityScoreDTO updateDTO = new AbilityScoreDTO();
        updateDTO.setCharisma(999);

        when(abilityScoreRepository.findById(1)).thenReturn(Optional.of(entity));
        when(abilityScoreRepository.existsById(1)).thenReturn(true);
        when(abilityScoreRepository.save(entity)).thenReturn(entity);
        when(abilityScoreMapper.mapToAbilityScoreDto(entity)).thenReturn(updateDTO);

        Optional<AbilityScoreDTO> result = abilityScoreService.updateAbilityScore(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals(999, result.get().getCharisma());
    }

    @Test
    public void whenAbilityScoreIdIsNotFound_updateAbilityScore_ReturnsEmptyOptional() {
        AbilityScoreDTO updateDTO = new AbilityScoreDTO();
        updateDTO.setCharisma(999);

        when(abilityScoreRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> abilityScoreService.updateAbilityScore(999, updateDTO));
    }

    @Test
    public void whenAbilityScoreIsInvalid_updateAbilityScore_DoesNotUpdate() {
        AbilityScoreDTO updateScore = new AbilityScoreDTO();
        updateScore.setConstitution(0);

        when(abilityScoreRepository.existsById(1)).thenReturn(true);
        Optional<AbilityScoreDTO> result = abilityScoreService.updateAbilityScore(1, updateScore);

        assertTrue(result.isEmpty(), "Update should fail with invalid input");
    }

    @Test
    public void whenAbilityScoreAlreadyExists_updateAbilityScore_ThrowsDataIntegrityViolationException() {
        AbilityScore score = abilityScoreMapper.mapFromAbilityScoreDto(dto);
        when(abilityScoreRepository.abilityScoreExists(score)).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> abilityScoreService.updateAbilityScore(entity.getId(), dto));
    }
}
