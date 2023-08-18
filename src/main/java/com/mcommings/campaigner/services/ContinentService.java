package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IContinent;
import com.mcommings.campaigner.models.Continent;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.IContinentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.ErrorMessage.*;

@Service
public class ContinentService implements IContinent {

    private final IContinentRepository continentRepository;

    @Autowired
    public ContinentService(IContinentRepository continentRepository) {
        this.continentRepository = continentRepository;
    }

    @Override
    public List<Continent> getContinents() {
        return continentRepository.findAll();
    }

    @Override
    @Transactional
    public void saveContinent(Continent continent) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.nameIsNullOrEmpty(continent)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if(RepositoryHelper.nameAlreadyExists(continentRepository, continent)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        continentRepository.saveAndFlush(continent);
    }

    @Override
    @Transactional
    public void deleteContinent(int continentId) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(continentRepository, continentId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: check if foreign key

        continentRepository.deleteById(continentId);
    }

    @Override
    @Transactional
    public void updateContinent(int continentId, Continent continent)
            throws IllegalArgumentException, DataIntegrityViolationException {

        if(RepositoryHelper.cannotFindId(continentRepository, continentId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }

        Continent continentToUpdate = RepositoryHelper.getById(continentRepository, continentId);
        continentToUpdate.setName(continent.getName());
        continentToUpdate.setDescription(continent.getDescription());
    }
}
