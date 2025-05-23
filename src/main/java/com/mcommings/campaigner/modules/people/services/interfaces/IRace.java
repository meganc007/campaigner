package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.dtos.RaceDTO;

import java.util.List;
import java.util.Optional;

public interface IRace {

    List<RaceDTO> getRaces();

    Optional<RaceDTO> getRace(int raceId);

    List<RaceDTO> getRacesByIsExotic(boolean isExotic);

    void saveRace(RaceDTO race);

    void deleteRace(int raceId);

    Optional<RaceDTO> updateRace(int raceId, RaceDTO race);
}
