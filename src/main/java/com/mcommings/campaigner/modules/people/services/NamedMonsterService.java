package com.mcommings.campaigner.modules.people.services;

import com.mcommings.campaigner.modules.common.entities.RepositoryHelper;
import com.mcommings.campaigner.modules.common.repositories.IWealthRepository;
import com.mcommings.campaigner.modules.people.entities.NamedMonster;
import com.mcommings.campaigner.modules.people.repositories.IAbilityScoreRepository;
import com.mcommings.campaigner.modules.people.repositories.IGenericMonsterRepository;
import com.mcommings.campaigner.modules.people.repositories.INamedMonsterRepository;
import com.mcommings.campaigner.modules.people.services.interfaces.INamedMonster;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.*;
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
    public List<NamedMonster> getNamedMonstersByCampaignUUID(UUID uuid) {
        return namedMonsterRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    public List<NamedMonster> getNamedMonstersByGenericMonster(int id) {
        return namedMonsterRepository.findByfk_generic_monster(id);
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
                RepositoryHelper.foreignKeyIsNotValid(buildReposAndColumnsHashMap(namedMonster), namedMonster)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }
        NamedMonster namedMonsterToUpdate = RepositoryHelper.getById(namedMonsterRepository, namedMonsterId);
        if (namedMonster.getFirstName() != null) namedMonsterToUpdate.setFirstName(namedMonster.getFirstName());
        if (namedMonster.getLastName() != null) namedMonsterToUpdate.setLastName(namedMonster.getLastName());
        if (namedMonster.getTitle() != null) namedMonsterToUpdate.setTitle(namedMonster.getTitle());
        if (namedMonster.getFk_wealth() != null) namedMonsterToUpdate.setFk_wealth(namedMonster.getFk_wealth());
        if (namedMonster.getFk_ability_score() != null)
            namedMonsterToUpdate.setFk_ability_score(namedMonster.getFk_ability_score());
        if (namedMonster.getFk_generic_monster() != null)
            namedMonsterToUpdate.setFk_generic_monster(namedMonster.getFk_generic_monster());
        if (namedMonster.getIsEnemy() != null) namedMonsterToUpdate.setIsEnemy(namedMonster.getIsEnemy());
        if (namedMonster.getPersonality() != null) namedMonsterToUpdate.setPersonality(namedMonster.getPersonality());
        if (namedMonster.getDescription() != null) namedMonsterToUpdate.setDescription(namedMonster.getDescription());
        if (namedMonster.getNotes() != null) namedMonsterToUpdate.setNotes(namedMonster.getNotes());
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

    private HashMap<CrudRepository, String> buildReposAndColumnsHashMap(NamedMonster namedMonster) {
        HashMap<CrudRepository, String> reposAndColumns = new HashMap<>();

        if (namedMonster.getFk_wealth() != null) {
            reposAndColumns.put(wealthRepository, FK_WEALTH.columnName);
        }
        if (namedMonster.getFk_ability_score() != null) {
            reposAndColumns.put(abilityScoreRepository, FK_ABILITY_SCORE.columnName);
        }
        if (namedMonster.getFk_generic_monster() != null) {
            reposAndColumns.put(genericMonsterRepository, FK_GENERIC_MONSTER.columnName);
        }
        return reposAndColumns;
    }

}
