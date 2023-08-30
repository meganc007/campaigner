package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.interfaces.calendar.ISun;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.calendar.Sun;
import com.mcommings.campaigner.models.repositories.calendar.ISunRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class SunService implements ISun {
    
    private final ISunRepository sunRepository;

    @Autowired
    public SunService(ISunRepository sunRepository) {
        this.sunRepository = sunRepository;
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
        //TODO: check if foreign key when Celestial Events added

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
}
