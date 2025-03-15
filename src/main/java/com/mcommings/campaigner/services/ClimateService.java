package com.mcommings.campaigner.services;

import com.mcommings.campaigner.entities.Climate;
import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.interfaces.IClimate;
import com.mcommings.campaigner.locations.repositories.IRegionRepository;
import com.mcommings.campaigner.repositories.IClimateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_CLIMATE;

@Service
public class ClimateService implements IClimate {

    private final IClimateRepository climateRepository;
    private final IRegionRepository regionRepository;

    @Autowired
    public ClimateService(IClimateRepository climateRepository, IRegionRepository regionRepository) {
        this.climateRepository = climateRepository;
        this.regionRepository = regionRepository;
    }

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
        if (RepositoryHelper.isForeignKey(getReposWhereClimateIsAForeignKey(), FK_CLIMATE.columnName, climateId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        climateRepository.deleteById(climateId);
    }

    @Override
    @Transactional
    public void updateClimate(int climateId, Climate climate) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(climateRepository, climateId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Climate climateToUpdate = RepositoryHelper.getById(climateRepository, climateId);
        if (climate.getName() != null) climateToUpdate.setName(climate.getName());
        if (climate.getDescription() != null) climateToUpdate.setDescription(climate.getDescription());
    }

    private List<CrudRepository> getReposWhereClimateIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(regionRepository));
    }
}
