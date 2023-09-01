package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.interfaces.calendar.ISun;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.calendar.Sun;
import com.mcommings.campaigner.repositories.calendar.ICelestialEventRepository;
import com.mcommings.campaigner.repositories.calendar.ISunRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_SUN;

@Service
public class SunService implements ISun {
    
    private final ISunRepository sunRepository;
    private final ICelestialEventRepository celestialEventRepository;

    @Autowired
    public SunService(ISunRepository sunRepository, ICelestialEventRepository celestialEventRepository) {
        this.sunRepository = sunRepository;
        this.celestialEventRepository = celestialEventRepository;
    }

    @Override
    public List<Sun> getSuns() {
        return sunRepository.findAll();
    }

    @Override
    @Transactional
    public void saveSun(Sun sun) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(sun)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(sunRepository, sun)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        sunRepository.saveAndFlush(sun);
    }

    @Override
    @Transactional
    public void deleteSun(int sunId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(sunRepository, sunId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereSunIsAForeignKey(), FK_SUN.columnName, sunId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }
        sunRepository.deleteById(sunId);
    }

    @Override
    @Transactional
    public void updateSun(int sunId, Sun sun) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(sunRepository, sunId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Sun sunToUpdate = RepositoryHelper.getById(sunRepository, sunId);
        sunToUpdate.setName(sun.getName());
        sunToUpdate.setDescription(sun.getDescription());
    }

    private List<CrudRepository> getReposWhereSunIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(celestialEventRepository));
    }
}
