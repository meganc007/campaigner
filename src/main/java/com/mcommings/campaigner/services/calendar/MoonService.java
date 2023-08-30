package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.interfaces.calendar.IMoon;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.calendar.Moon;
import com.mcommings.campaigner.models.repositories.calendar.IMoonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class MoonService implements IMoon {

    private final IMoonRepository moonRepository;

    @Autowired
    public MoonService(IMoonRepository moonRepository) {
        this.moonRepository = moonRepository;
    }

    @Override
    public List<Moon> getMoons() {
        return moonRepository.findAll();
    }

    @Override
    @Transactional
    public void saveMoon(Moon moon) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(moon)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(moonRepository, moon)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        moonRepository.saveAndFlush(moon);
    }

    @Override
    @Transactional
    public void deleteMoon(int moonId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(moonRepository, moonId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: check if foreign key when Celestial Events added

        moonRepository.deleteById(moonId);
    }

    @Override
    @Transactional
    public void updateMoon(int moonId, Moon moon) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(moonRepository, moonId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Moon moonToUpdate = RepositoryHelper.getById(moonRepository, moonId);
        moonToUpdate.setName(moon.getName());
        moonToUpdate.setDescription(moon.getDescription());
    }
}
