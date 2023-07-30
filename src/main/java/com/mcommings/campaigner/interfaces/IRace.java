package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Race;

import java.util.List;
import java.util.Optional;

public interface IRace {

    public List<Race> getRaces();

    public void saveRace(Race race);

    public boolean raceAlreadyExists(Race race);

    public boolean raceNameIsNull(Race race);
}
