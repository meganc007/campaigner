package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Race;

import java.util.List;

public interface IRace {

    public List<Race> getRaces();

    public void saveRace(Race race);

    public void deleteRace(int raceId);

    public void updateRace(int raceId, Race race);

    public boolean raceAlreadyExists(Race race);

    public boolean raceNameIsNull(Race race);

}
