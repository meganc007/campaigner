package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IRace;
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
    public boolean raceAlreadyExists(Race race) {
        Optional<Race> existingRace = raceRepository.findRaceByName(race.getName());
        return existingRace.isPresent();
    }

    @Override
    public boolean raceNameIsNull(Race race) {
        return isNull(race.getName());
    }

    @Override
    @Transactional
    public void saveRace(Race race) throws IllegalArgumentException, DataIntegrityViolationException {

        if(raceNameIsNull(race)) {
            throw new IllegalArgumentException("Race name cannot be null.");
        }
        if(raceAlreadyExists(race)) {
            throw new DataIntegrityViolationException("Race already exists.");
        }
        raceRepository.saveAndFlush(race);
    }

}
