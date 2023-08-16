package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Climate;

import java.util.List;

public interface IClimate {

    public List<Climate> getClimates();

    public void saveClimate(Climate climate);

    public void deleteClimate(int climateId);

    public void updateClimate(int climateId, Climate climate);
}
