package com.mcommings.campaigner.modules.common.services.interfaces;

import com.mcommings.campaigner.modules.common.entities.Climate;

import java.util.List;

public interface IClimate {

    List<Climate> getClimates();

    void saveClimate(Climate climate);

    void deleteClimate(int climateId);

    void updateClimate(int climateId, Climate climate);
}
