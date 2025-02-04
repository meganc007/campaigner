package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.people.Race;
import com.mcommings.campaigner.interfaces.people.IRace;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import com.mcommings.campaigner.repositories.people.IRaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_RACE;

@Service
public class RaceService implements IRace {

    private final IRaceRepository raceRepository;
    private final IPersonRepository personRepository;

    @Autowired
    public RaceService(IRaceRepository raceRepository, IPersonRepository personRepository) {
        this.raceRepository = raceRepository;
        this.personRepository = personRepository;
    }

    @Override
    public List<Race> getRaces() {
        return raceRepository.findAll();
    }

    @Override
    @Transactional
    public void saveRace(Race race) throws IllegalArgumentException, DataIntegrityViolationException {

        if (RepositoryHelper.nameIsNullOrEmpty(race)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(raceRepository, race)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        raceRepository.saveAndFlush(race);
    }

    @Override
    @Transactional
    public void deleteRace(int raceId) throws IllegalArgumentException, DataIntegrityViolationException {

        if (RepositoryHelper.cannotFindId(raceRepository, raceId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereRaceIsAForeignKey(), FK_RACE.columnName, raceId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        raceRepository.deleteById(raceId);
    }

    @Override
    @Transactional
    public void updateRace(int raceId, Race race) throws IllegalArgumentException, DataIntegrityViolationException {

        if (RepositoryHelper.cannotFindId(raceRepository, raceId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Race raceToUpdate = RepositoryHelper.getById(raceRepository, raceId);
        if (race.getName() != null) raceToUpdate.setName(race.getName());
        if (race.getDescription() != null) raceToUpdate.setDescription(race.getDescription());
        if (race.getIs_exotic() != null) raceToUpdate.setIs_exotic(race.getIs_exotic());
    }

    private List<CrudRepository> getReposWhereRaceIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(personRepository));
    }

}
