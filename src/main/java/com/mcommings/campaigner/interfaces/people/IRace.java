package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.models.people.Race;

import java.util.List;

public interface IRace {

    List<Race> getRaces();

    void saveRace(Race race);

    void deleteRace(int raceId);

    void updateRace(int raceId, Race race);

}
