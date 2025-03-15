package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.entities.Race;

import java.util.List;

public interface IRace {

    List<Race> getRaces();

    void saveRace(Race race);

    void deleteRace(int raceId);

    void updateRace(int raceId, Race race);

}
