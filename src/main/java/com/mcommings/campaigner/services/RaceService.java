package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IRace;
import com.mcommings.campaigner.models.IRaceRepository;
import com.mcommings.campaigner.models.Race;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceService implements IRace {

    private final IRaceRepository raceRepository;

    @Autowired
    public RaceService(IRaceRepository raceRepository) {this.raceRepository = raceRepository;}

    @Override
    public List<Race> getRaces() {
        return raceRepository.findAll();
    }
}
