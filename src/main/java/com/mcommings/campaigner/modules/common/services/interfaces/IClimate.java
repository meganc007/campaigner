package com.mcommings.campaigner.modules.common.services.interfaces;

import com.mcommings.campaigner.modules.common.dtos.ClimateDTO;

import java.util.List;
import java.util.Optional;

public interface IClimate {

    List<ClimateDTO> getClimates();

    Optional<ClimateDTO> getClimate(int climateId);

    void saveClimate(ClimateDTO climate);

    void deleteClimate(int climateId);

    Optional<ClimateDTO> updateClimate(int climateId, ClimateDTO climate);
}
