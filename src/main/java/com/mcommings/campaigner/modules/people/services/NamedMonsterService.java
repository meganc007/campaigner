package com.mcommings.campaigner.modules.people.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.people.dtos.NamedMonsterDTO;
import com.mcommings.campaigner.modules.people.entities.NamedMonster;
import com.mcommings.campaigner.modules.people.mappers.NamedMonsterMapper;
import com.mcommings.campaigner.modules.people.repositories.INamedMonsterRepository;
import com.mcommings.campaigner.modules.people.services.interfaces.INamedMonster;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class NamedMonsterService implements INamedMonster {

    private final INamedMonsterRepository namedMonsterRepository;
    private final NamedMonsterMapper namedMonsterMapper;

    @Override
    public List<NamedMonsterDTO> getNamedMonsters() {

        return namedMonsterRepository.findAll()
                .stream()
                .map(namedMonsterMapper::mapToNamedMonsterDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NamedMonsterDTO> getNamedMonster(int namedMonsterId) {
        return namedMonsterRepository.findById(namedMonsterId)
                .map(namedMonsterMapper::mapToNamedMonsterDto);
    }

    @Override
    public List<NamedMonsterDTO> getNamedMonstersByCampaignUUID(UUID uuid) {
        return namedMonsterRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(namedMonsterMapper::mapToNamedMonsterDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NamedMonsterDTO> getNamedMonstersByGenericMonster(int id) {
        return namedMonsterRepository.findByfk_generic_monster(id)
                .stream()
                .map(namedMonsterMapper::mapToNamedMonsterDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NamedMonsterDTO> getNamedMonstersByAbilityScore(int abilityScoreId) {
        return namedMonsterRepository.findByfk_ability_score(abilityScoreId)
                .stream()
                .map(namedMonsterMapper::mapToNamedMonsterDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NamedMonsterDTO> getNamedMonstersByEnemyStatus(boolean isEnemy) {
        return namedMonsterRepository.findByIsEnemy(isEnemy)
                .stream()
                .map(namedMonsterMapper::mapToNamedMonsterDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveNamedMonster(NamedMonsterDTO namedMonster) throws IllegalArgumentException, DataIntegrityViolationException {
        if (nameIsNullOrEmpty(namedMonster.getFirstName())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (namedMonsterAlreadyExists(namedMonsterMapper.mapFromNamedMonsterDto(namedMonster))) {
            throw new DataIntegrityViolationException(NAMED_MONSTER_EXISTS.message);
        }

        namedMonsterMapper.mapToNamedMonsterDto(
                namedMonsterRepository.save(namedMonsterMapper.mapFromNamedMonsterDto(namedMonster)
                ));
    }

    @Override
    public void deleteNamedMonster(int namedMonsterId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(namedMonsterRepository, namedMonsterId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        namedMonsterRepository.deleteById(namedMonsterId);
    }

    @Override
    public Optional<NamedMonsterDTO> updateNamedMonster(int namedMonsterId, NamedMonsterDTO namedMonster) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(namedMonsterRepository, namedMonsterId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (nameIsNullOrEmpty(namedMonster.getFirstName())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (nameAlreadyExists(namedMonster)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return namedMonsterRepository.findById(namedMonsterId).map(foundNamedMonster -> {
            if (namedMonster.getFirstName() != null) foundNamedMonster.setFirstName(namedMonster.getFirstName());
            if (namedMonster.getLastName() != null) foundNamedMonster.setLastName(namedMonster.getLastName());
            if (namedMonster.getTitle() != null) foundNamedMonster.setTitle(namedMonster.getTitle());
            if (namedMonster.getFk_wealth() != null) foundNamedMonster.setFk_wealth(namedMonster.getFk_wealth());
            if (namedMonster.getFk_ability_score() != null)
                foundNamedMonster.setFk_ability_score(namedMonster.getFk_ability_score());
            if (namedMonster.getFk_generic_monster() != null)
                foundNamedMonster.setFk_generic_monster(namedMonster.getFk_generic_monster());
            if (namedMonster.getIsEnemy() != null) foundNamedMonster.setIsEnemy(namedMonster.getIsEnemy());
            if (namedMonster.getPersonality() != null) foundNamedMonster.setPersonality(namedMonster.getPersonality());
            if (namedMonster.getDescription() != null) foundNamedMonster.setDescription(namedMonster.getDescription());
            if (namedMonster.getNotes() != null) foundNamedMonster.setNotes(namedMonster.getNotes());

            return namedMonsterMapper.mapToNamedMonsterDto(namedMonsterRepository.save(foundNamedMonster));
        });
    }

    private boolean nameIsNullOrEmpty(String name) {
        return isNull(name) || name.isEmpty();
    }

    private boolean namedMonsterAlreadyExists(NamedMonster namedMonster) {
        return namedMonsterRepository.monsterExists(namedMonster).isPresent();
    }

    private boolean nameAlreadyExists(NamedMonsterDTO namedMonster) {
        return namedMonsterRepository
                .findByFirstNameAndLastName(namedMonster.getFirstName(), namedMonster.getLastName())
                .isPresent();
    }
}
