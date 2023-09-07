package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.interfaces.people.INamedMonster;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.people.NamedMonster;
import com.mcommings.campaigner.repositories.IWealthRepository;
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
import static java.util.Objects.isNull;

@Service
public class NamedMonsterService implements INamedMonster {

    private final INamedMonsterRepository namedMonsterRepository;
    private final IWealthRepository wealthRepository;
    private final IAbilityScoreRepository abilityScoreRepository;
    private final IGenericMonsterRepository genericMonsterRepository;

    @Autowired
    public NamedMonsterService(INamedMonsterRepository namedMonsterRepository, IWealthRepository wealthRepository,
                               IAbilityScoreRepository abilityScoreRepository, IGenericMonsterRepository genericMonsterRepository) {
        this.namedMonsterRepository = namedMonsterRepository;
        this.wealthRepository = wealthRepository;
        this.abilityScoreRepository = abilityScoreRepository;
        this.genericMonsterRepository = genericMonsterRepository;
    }

    @Override
    public List<NamedMonster> getNamedMonsters() {
        return namedMonsterRepository.findAll();
    }

    @Override
    @Transactional
    public void saveNamedMonster(NamedMonster namedMonster) throws IllegalArgumentException, DataIntegrityViolationException {
        if (nameIsNullOrEmpty(namedMonster.getFirstName()) || nameIsNullOrEmpty(namedMonster.getLastName())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (namedMonsterAlreadyExists(namedMonster)) {
            throw new DataIntegrityViolationException(NAMED_MONSTER_EXISTS.message);
        }
        if (hasForeignKeys(namedMonster) &&
                RepositoryHelper.foreignKeyIsNotValid(namedMonsterRepository, getListOfForeignKeyRepositories(), namedMonster)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        namedMonsterRepository.saveAndFlush(namedMonster);
    }

    @Override
    @Transactional
    public void deleteNamedMonster(int namedMonsterId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(namedMonsterRepository, namedMonsterId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
//        TODO: uncomment/fix when NamedMonster is used as a fk
//        if (RepositoryHelper.isForeignKey(getReposWhereNamedMonsterIsAForeignKey(), FK_NAMED_MONSTER.columnName, namedMonsterId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }
        namedMonsterRepository.deleteById(namedMonsterId);
    }

    @Override
    @Transactional
    public void updateNamedMonster(int namedMonsterId, NamedMonster namedMonster) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(namedMonsterRepository, namedMonsterId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(namedMonster) &&
                RepositoryHelper.foreignKeyIsNotValid(namedMonsterRepository, getListOfForeignKeyRepositories(), namedMonster)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        NamedMonster namedMonsterToUpdate = RepositoryHelper.getById(namedMonsterRepository, namedMonsterId);
        namedMonsterToUpdate.setFirstName(namedMonster.getFirstName());
        namedMonsterToUpdate.setLastName(namedMonster.getLastName());
        namedMonsterToUpdate.setTitle(namedMonster.getTitle());
        namedMonsterToUpdate.setFk_wealth(namedMonster.getFk_wealth());
        namedMonsterToUpdate.setFk_ability_score(namedMonster.getFk_ability_score());
        namedMonsterToUpdate.setFk_generic_monster(namedMonster.getFk_generic_monster());
        namedMonsterToUpdate.setIsEnemy(namedMonster.getIsEnemy());
        namedMonsterToUpdate.setPersonality(namedMonster.getPersonality());
        namedMonsterToUpdate.setDescription(namedMonster.getDescription());
        namedMonsterToUpdate.setNotes(namedMonster.getNotes());
    }

    private boolean nameIsNullOrEmpty(String name) {
        return isNull(name) || name.isEmpty();
    }

    private boolean namedMonsterAlreadyExists(NamedMonster namedMonster) {
        return namedMonsterRepository.monsterExists(namedMonster).isPresent();
    }

//    TODO: uncomment/fix when NamedMonster is used as a fk
//    private List<CrudRepository> getReposWhereNamedMonsterIsAForeignKey() {
//        return new ArrayList<>(Arrays.asList());
//    }

    private boolean hasForeignKeys(NamedMonster namedMonster) {
        return namedMonster.getFk_wealth() != null ||
                namedMonster.getFk_ability_score() != null ||
                namedMonster.getFk_generic_monster() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(wealthRepository, abilityScoreRepository, genericMonsterRepository));
    }

}
