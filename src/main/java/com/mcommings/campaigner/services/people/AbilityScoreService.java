package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.interfaces.people.IAbilityScore;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.people.AbilityScore;
import com.mcommings.campaigner.repositories.people.IAbilityScoreRepository;
import com.mcommings.campaigner.repositories.people.IGenericMonsterRepository;
import com.mcommings.campaigner.repositories.people.INamedMonsterRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_ABILITY_SCORE;

@Service
public class AbilityScoreService implements IAbilityScore {

    private final IAbilityScoreRepository abilityScoreRepository;
    private final IPersonRepository personRepository;
    private final IGenericMonsterRepository genericMonsterRepository;
    private final INamedMonsterRepository namedMonsterRepository;

    @Autowired
    public AbilityScoreService(IAbilityScoreRepository abilityScoreRepository, IPersonRepository personRepository,
                               IGenericMonsterRepository genericMonsterRepository, INamedMonsterRepository namedMonsterRepository) {
        this.abilityScoreRepository = abilityScoreRepository;
        this.personRepository = personRepository;
        this.genericMonsterRepository = genericMonsterRepository;
        this.namedMonsterRepository = namedMonsterRepository;
    }

    @Override
    public List<AbilityScore> getAbilityScores() {
        return abilityScoreRepository.findAll();
    }

    @Override
    @Transactional
    public void saveAbilityScore(AbilityScore abilityScore) throws IllegalArgumentException, DataIntegrityViolationException {
        if (abilityScoreEqualsZero(abilityScore)) {
            throw new IllegalArgumentException(CANNOT_BE_ZERO.message);
        }
        if (abilityScoreAlreadyExists(abilityScore)) {
            throw new DataIntegrityViolationException(SCORE_EXISTS.message);
        }

        abilityScoreRepository.saveAndFlush(abilityScore);
    }

    @Override
    @Transactional
    public void deleteAbilityScore(int id) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(abilityScoreRepository, id)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereAbilityScoreIsAForeignKey(), FK_ABILITY_SCORE.columnName, id)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        abilityScoreRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateAbilityScore(int id, AbilityScore abilityScore) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(abilityScoreRepository, id)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        AbilityScore abilityScoreToUpdate = RepositoryHelper.getById(abilityScoreRepository, id);
        abilityScoreToUpdate.setStrength(abilityScore.getStrength());
        abilityScoreToUpdate.setDexterity(abilityScore.getDexterity());
        abilityScoreToUpdate.setConstitution(abilityScore.getConstitution());
        abilityScoreToUpdate.setIntelligence(abilityScore.getIntelligence());
        abilityScoreToUpdate.setWisdom(abilityScore.getWisdom());
        abilityScoreToUpdate.setCharisma(abilityScore.getCharisma());
    }

    private boolean abilityScoreEqualsZero(AbilityScore abilityScore) {
        return abilityScore.getStrength() == 0 ||
                abilityScore.getDexterity() == 0 ||
                abilityScore.getConstitution() == 0 ||
                abilityScore.getIntelligence() == 0 ||
                abilityScore.getWisdom() == 0 ||
                abilityScore.getCharisma() == 0;
    }

    private boolean abilityScoreAlreadyExists(AbilityScore abilityScore) {
        return abilityScoreRepository.abilityScoreExists(abilityScore).isPresent();
    }

    private List<CrudRepository> getReposWhereAbilityScoreIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(personRepository, genericMonsterRepository, namedMonsterRepository));
    }
}
