package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IClimate;
import com.mcommings.campaigner.models.Climate;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.IClimateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Override
    @Transactional
    public void saveClimate(Climate climate) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.nameIsNullOrEmpty(climate)) {
            throw new IllegalArgumentException("Climate name cannot be null or empty.");
        }
        if(RepositoryHelper.nameAlreadyExists(climateRepository, climate)) {
            throw new DataIntegrityViolationException("Climate already exists.");
        }

        climateRepository.saveAndFlush(climate);
    }

    @Override
    public void deleteClimate(int id) {

    }

    @Override
    public void updateClimate(int id, Climate climate) {

    }
}
