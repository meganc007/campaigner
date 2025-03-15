package com.mcommings.campaigner.calendar.services;

import com.mcommings.campaigner.calendar.entities.Moon;
import com.mcommings.campaigner.calendar.repositories.ICelestialEventRepository;
import com.mcommings.campaigner.calendar.repositories.IMoonRepository;
import com.mcommings.campaigner.calendar.services.interfaces.IMoon;
import com.mcommings.campaigner.entities.RepositoryHelper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_MOON;

@SuppressWarnings("rawtypes")
@Service
public class MoonService implements IMoon {

    private final IMoonRepository moonRepository;
    private final ICelestialEventRepository celestialEventRepository;

    @Autowired
    public MoonService(IMoonRepository moonRepository, ICelestialEventRepository celestialEventRepository) {
        this.moonRepository = moonRepository;
        this.celestialEventRepository = celestialEventRepository;
    }

    @Override
    public List<Moon> getMoons() {
        return moonRepository.findAll();
    }

    @Override
    public List<Moon> getMoonsByCampaignUUID(UUID uuid) {
        return moonRepository.findByfk_campaign_uuid(uuid);
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
        if (RepositoryHelper.isForeignKey(getReposWhereMoonIsAForeignKey(), FK_MOON.columnName, moonId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        moonRepository.deleteById(moonId);
    }

    @Override
    @Transactional
    public void updateMoon(int moonId, Moon moon) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(moonRepository, moonId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Moon moonToUpdate = RepositoryHelper.getById(moonRepository, moonId);
        if (moon.getName() != null) moonToUpdate.setName(moon.getName());
        if (moon.getDescription() != null) moonToUpdate.setDescription(moon.getDescription());
    }

    private List<CrudRepository> getReposWhereMoonIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(celestialEventRepository));
    }
}
