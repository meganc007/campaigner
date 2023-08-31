package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IClimate;
import com.mcommings.campaigner.models.Climate;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.repositories.IClimateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

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
        if (RepositoryHelper.nameIsNullOrEmpty(climate)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(climateRepository, climate)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        climateRepository.saveAndFlush(climate);
    }

    @Override
    @Transactional
    public void deleteClimate(int climateId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(climateRepository, climateId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: check that Climate isn't a foreign key before deleting

        climateRepository.deleteById(climateId);
    }

    @Override
    @Transactional
    public void updateClimate(int climateId, Climate climate) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(climateRepository, climateId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Climate climateToUpdate = RepositoryHelper.getById(climateRepository, climateId);
        climateToUpdate.setName(climate.getName());
        climateToUpdate.setDescription(climate.getDescription());
    }
}
