package com.mcommings.campaigner.people.services.interfaces;

import com.mcommings.campaigner.people.entities.Race;

import java.util.List;

public interface IRace {

    List<Race> getRaces();

    void saveRace(Race race);

    void deleteRace(int raceId);

    void updateRace(int raceId, Race race);

}
