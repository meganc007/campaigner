package com.mcommings.campaigner.modules.people.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.people.dtos.GenericMonsterDTO;
import com.mcommings.campaigner.modules.people.entities.GenericMonster;
import com.mcommings.campaigner.modules.people.mappers.GenericMonsterMapper;
import com.mcommings.campaigner.modules.people.repositories.IGenericMonsterRepository;
import com.mcommings.campaigner.modules.people.services.interfaces.IGenericMonster;
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
public class GenericMonsterService implements IGenericMonster {

    private final IGenericMonsterRepository genericMonsterRepository;
    private final GenericMonsterMapper genericMonsterMapper;

    @Override
    public List<GenericMonsterDTO> getGenericMonsters() {

        return genericMonsterRepository.findAll()
                .stream()
                .map(genericMonsterMapper::mapToGenericMonsterDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GenericMonsterDTO> getGenericMonster(int genericMonsterId) {
        return genericMonsterRepository.findById(genericMonsterId)
                .map(genericMonsterMapper::mapToGenericMonsterDto);
    }

    @Override
    public List<GenericMonsterDTO> getGenericMonstersByCampaignUUID(UUID uuid) {
        return genericMonsterRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(genericMonsterMapper::mapToGenericMonsterDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GenericMonsterDTO> getGenericMonstersByAbilityScore(int abilityScoreId) {
        return genericMonsterRepository.findByfk_ability_score(abilityScoreId)
                .stream()
                .map(genericMonsterMapper::mapToGenericMonsterDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveGenericMonster(GenericMonsterDTO genericMonster) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(genericMonster)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(genericMonsterRepository, genericMonster.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        genericMonsterMapper.mapToGenericMonsterDto(
                genericMonsterRepository.save(genericMonsterMapper.mapFromGenericMonsterDto(genericMonster)
                ));
    }

    @Override
    public void deleteGenericMonster(int genericMonsterId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(genericMonsterRepository, genericMonsterId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        genericMonsterRepository.deleteById(genericMonsterId);
    }

    @Override
    public Optional<GenericMonsterDTO> updateGenericMonster(int genericMonsterId, GenericMonsterDTO genericMonster) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(genericMonsterRepository, genericMonsterId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(genericMonster)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(genericMonsterRepository, genericMonster.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return genericMonsterRepository.findById(genericMonsterId).map(foundGenericMonster -> {
            if (genericMonster.getName() != null) foundGenericMonster.setName(genericMonster.getName());
            if (genericMonster.getFk_ability_score() != null)
                foundGenericMonster.setFk_ability_score(genericMonster.getFk_ability_score());
            if (genericMonster.getTraits() != null) foundGenericMonster.setTraits(genericMonster.getTraits());
            if (genericMonster.getDescription() != null)
                foundGenericMonster.setDescription(genericMonster.getDescription());
            if (genericMonster.getNotes() != null) foundGenericMonster.setNotes(genericMonster.getNotes());

            return genericMonsterMapper.mapToGenericMonsterDto(genericMonsterRepository.save(foundGenericMonster));
        });
    }

    private boolean hasForeignKeys(GenericMonster genericMonster) {
        return genericMonster.getFk_ability_score() != null;
    }
}
