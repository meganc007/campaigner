package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IRace;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.IRaceRepository;
import com.mcommings.campaigner.models.Race;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class RaceService implements IRace {

    private final IRaceRepository raceRepository;

    @Autowired
    public RaceService(IRaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    @Override
    public List<Race> getRaces() {
        return raceRepository.findAll();
    }

    @Override
    @Transactional
    public void saveRace(Race race) throws IllegalArgumentException, DataIntegrityViolationException {

        if(RepositoryHelper.nameIsNullOrEmpty(race)) {
            throw new IllegalArgumentException("Race name cannot be null or empty.");
        }
        if(RepositoryHelper.nameAlreadyExists(raceRepository, race)) {
            throw new DataIntegrityViolationException("Race already exists.");
        }

        raceRepository.saveAndFlush(race);
    }

    @Override
    @Transactional
    public void deleteRace(int raceId) throws IllegalArgumentException, DataIntegrityViolationException {

        if (RepositoryHelper.cannotFindId(raceRepository, raceId)) {
            throw new IllegalArgumentException("Unable to delete; This race was not found.");
        }
// TODO: after adding the People table/repo, check that Race id isn't a foreign key before deleting
//        if(raceUsedAsForeignKey) {
//            throw new DataIntegrityViolationException("Unable to delete; This race is used in another table.");
//        }

        raceRepository.deleteById(raceId);
    }

    @Override
    @Transactional
    public void updateRace(int raceId, Race race) throws IllegalArgumentException, DataIntegrityViolationException {

        if(RepositoryHelper.cannotFindId(raceRepository, raceId)) {
            throw new IllegalArgumentException("Unable to update; This race was not found.");
        }
        Race raceToUpdate = RepositoryHelper.getById(raceRepository, raceId);
        raceToUpdate.setName(race.getName());
        raceToUpdate.setDescription(race.getDescription());
        raceToUpdate.set_exotic(race.is_exotic());
    }

}
