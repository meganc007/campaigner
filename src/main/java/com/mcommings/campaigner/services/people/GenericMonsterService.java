package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.interfaces.people.IGenericMonster;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.people.GenericMonster;
import com.mcommings.campaigner.repositories.people.IAbilityScoreRepository;
import com.mcommings.campaigner.repositories.people.IGenericMonsterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class GenericMonsterService implements IGenericMonster {

    private final IGenericMonsterRepository genericMonsterRepository;
    private final IAbilityScoreRepository abilityScoreRepository;

    @Autowired
    public GenericMonsterService(IGenericMonsterRepository genericMonsterRepository, IAbilityScoreRepository abilityScoreRepository) {
        this.genericMonsterRepository = genericMonsterRepository;
        this.abilityScoreRepository = abilityScoreRepository;
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
// TODO: uncomment when class that uses GenericMonster as a fk is added
//        if (RepositoryHelper.isForeignKey(getReposWhereGenericMonsterIsAForeignKey(), FK_GENERIC_MONSTER.columnName, genericMonsterId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

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
        genericMonsterToUpdate.setName(genericMonster.getName());
        genericMonsterToUpdate.setFk_ability_score(genericMonster.getFk_ability_score());
        genericMonsterToUpdate.setTraits(genericMonster.getTraits());
        genericMonsterToUpdate.setDescription(genericMonster.getDescription());
        genericMonsterToUpdate.setNotes(genericMonster.getNotes());
    }

// TODO: uncomment when class that uses GenericMonster as a fk is added
//    private List<CrudRepository> getReposWhereGenericMonsterIsAForeignKey() {
//        return new ArrayList<>(Arrays.asList());
//    }

    private boolean hasForeignKeys(GenericMonster genericMonster) {
        return genericMonster.getFk_ability_score() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(abilityScoreRepository));
    }
}
