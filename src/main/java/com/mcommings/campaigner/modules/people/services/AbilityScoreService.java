package com.mcommings.campaigner.modules.people.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.people.dtos.AbilityScoreDTO;
import com.mcommings.campaigner.modules.people.entities.AbilityScore;
import com.mcommings.campaigner.modules.people.mappers.AbilityScoreMapper;
import com.mcommings.campaigner.modules.people.repositories.IAbilityScoreRepository;
import com.mcommings.campaigner.modules.people.services.interfaces.IAbilityScore;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class AbilityScoreService implements IAbilityScore {

    private final IAbilityScoreRepository abilityScoreRepository;
    private final AbilityScoreMapper abilityScoreMapper;


    @Override
    public List<AbilityScoreDTO> getAbilityScores() {

        return abilityScoreRepository.findAll()
                .stream()
                .map(abilityScoreMapper::mapToAbilityScoreDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AbilityScoreDTO> getAbilityScore(int id) {
        return abilityScoreRepository.findById(id)
                .map(abilityScoreMapper::mapToAbilityScoreDto);
    }

    @Override
    public List<AbilityScoreDTO> getAbilityScoresByCampaignUUID(UUID uuid) {
        return abilityScoreRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(abilityScoreMapper::mapToAbilityScoreDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAbilityScore(AbilityScoreDTO abilityScore) throws IllegalArgumentException, DataIntegrityViolationException {
        if (abilityScoreEqualsZero(abilityScore)) {
            throw new IllegalArgumentException(CANNOT_BE_ZERO.message);
        }
        if (abilityScoreAlreadyExists(abilityScore)) {
            throw new DataIntegrityViolationException(SCORE_EXISTS.message);
        }

        abilityScoreMapper.mapToAbilityScoreDto(
                abilityScoreRepository.save(
                        abilityScoreMapper.mapFromAbilityScoreDto(abilityScore))
        );
    }

    @Override
    public void deleteAbilityScore(int id) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(abilityScoreRepository, id)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        abilityScoreRepository.deleteById(id);
    }

    @Override
    public Optional<AbilityScoreDTO> updateAbilityScore(int id, AbilityScoreDTO abilityScore) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(abilityScoreRepository, id)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (abilityScoreAlreadyExists(abilityScore)) {
            throw new DataIntegrityViolationException(SCORE_EXISTS.message);
        }

        return abilityScoreRepository.findById(id).map(foundAbilityScore -> {
            if (abilityScore.getStrength() >= 0) foundAbilityScore.setStrength(abilityScore.getStrength());
            if (abilityScore.getDexterity() >= 0) foundAbilityScore.setDexterity(abilityScore.getDexterity());
            if (abilityScore.getConstitution() >= 0) foundAbilityScore.setConstitution(abilityScore.getConstitution());
            if (abilityScore.getIntelligence() >= 0) foundAbilityScore.setIntelligence(abilityScore.getIntelligence());
            if (abilityScore.getWisdom() >= 0) foundAbilityScore.setWisdom(abilityScore.getWisdom());
            if (abilityScore.getCharisma() >= 0) foundAbilityScore.setCharisma(abilityScore.getCharisma());

            return abilityScoreMapper.mapToAbilityScoreDto(abilityScoreRepository.save(foundAbilityScore));
        });
    }

    private boolean abilityScoreEqualsZero(AbilityScoreDTO abilityScore) {
        return abilityScore.getStrength() == 0 ||
                abilityScore.getDexterity() == 0 ||
                abilityScore.getConstitution() == 0 ||
                abilityScore.getIntelligence() == 0 ||
                abilityScore.getWisdom() == 0 ||
                abilityScore.getCharisma() == 0;
    }

    private boolean abilityScoreAlreadyExists(AbilityScoreDTO abilityScore) {
        AbilityScore score = abilityScoreMapper.mapFromAbilityScoreDto(abilityScore);
        return abilityScoreRepository.abilityScoreExists(score).isPresent();
    }

}
