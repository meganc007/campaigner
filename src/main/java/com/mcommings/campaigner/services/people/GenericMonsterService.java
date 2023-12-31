package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.interfaces.people.IGenericMonster;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.people.GenericMonster;
import com.mcommings.campaigner.repositories.people.IAbilityScoreRepository;
import com.mcommings.campaigner.repositories.people.IGenericMonsterRepository;
import com.mcommings.campaigner.repositories.people.INamedMonsterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_GENERIC_MONSTER;

@Service
public class GenericMonsterService implements IGenericMonster {

    private final IGenericMonsterRepository genericMonsterRepository;
    private final IAbilityScoreRepository abilityScoreRepository;
    private final INamedMonsterRepository namedMonsterRepository;

    @Autowired
    public GenericMonsterService(IGenericMonsterRepository genericMonsterRepository,
                                 IAbilityScoreRepository abilityScoreRepository, INamedMonsterRepository namedMonsterRepository) {
        this.genericMonsterRepository = genericMonsterRepository;
        this.abilityScoreRepository = abilityScoreRepository;
        this.namedMonsterRepository = namedMonsterRepository;
    }

    @Override
    public List<GenericMonster> getGenericMonsters() {
        return genericMonsterRepository.findAll();
    }

    @Override
    @Transactional
    public void saveGenericMonster(GenericMonster genericMonster) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(genericMonster)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(genericMonsterRepository, genericMonster)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        if (hasForeignKeys(genericMonster) &&
                RepositoryHelper.foreignKeyIsNotValid(genericMonsterRepository, getListOfForeignKeyRepositories(), genericMonster)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        genericMonsterRepository.saveAndFlush(genericMonster);
    }

    @Override
    @Transactional
    public void deleteGenericMonster(int genericMonsterId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(genericMonsterRepository, genericMonsterId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereGenericMonsterIsAForeignKey(), FK_GENERIC_MONSTER.columnName, genericMonsterId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        genericMonsterRepository.deleteById(genericMonsterId);
    }

    @Override
    @Transactional
    public void updateGenericMonster(int genericMonsterId, GenericMonster genericMonster) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(genericMonsterRepository, genericMonsterId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(genericMonster) &&
                RepositoryHelper.foreignKeyIsNotValid(genericMonsterRepository, getListOfForeignKeyRepositories(), genericMonster)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        GenericMonster genericMonsterToUpdate = RepositoryHelper.getById(genericMonsterRepository, genericMonsterId);
        if (genericMonster.getName() != null) genericMonsterToUpdate.setName(genericMonster.getName());
        if (genericMonster.getFk_ability_score() != null)
            genericMonsterToUpdate.setFk_ability_score(genericMonster.getFk_ability_score());
        if (genericMonster.getTraits() != null) genericMonsterToUpdate.setTraits(genericMonster.getTraits());
        if (genericMonster.getDescription() != null)
            genericMonsterToUpdate.setDescription(genericMonster.getDescription());
        if (genericMonster.getNotes() != null) genericMonsterToUpdate.setNotes(genericMonster.getNotes());
    }

    private List<CrudRepository> getReposWhereGenericMonsterIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(namedMonsterRepository));
    }

    private boolean hasForeignKeys(GenericMonster genericMonster) {
        return genericMonster.getFk_ability_score() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(abilityScoreRepository));
    }
}
