package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Race;

import java.util.List;

public interface IRace {

    List<Race> getRaces();

    void saveRace(Race race);

    void deleteRace(int raceId);

    void updateRace(int raceId, Race race);

}
