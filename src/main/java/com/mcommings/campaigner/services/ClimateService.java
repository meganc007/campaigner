package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IClimate;
import com.mcommings.campaigner.models.Climate;
import com.mcommings.campaigner.models.repositories.IClimateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClimateService implements IClimate {

    private final IClimateRepository climateRepository;

    @Autowired
    public ClimateService (IClimateRepository climateRepository) {this.climateRepository = climateRepository;}
    @Override
    public List<Climate> getClimates() {
        return climateRepository.findAll();
    }
}
