package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.interfaces.locations.IContinent;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.Continent;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.locations.IContinentRepository;
import com.mcommings.campaigner.repositories.locations.ICountryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_CONTINENT;

@Service
public class ContinentService implements IContinent {

    private final IContinentRepository continentRepository;
    private final ICountryRepository countryRepository;
    private final IEventRepository eventRepository;

    @Autowired
    public ContinentService(IContinentRepository continentRepository, ICountryRepository countryRepository,
                            IEventRepository eventRepository) {
        this.continentRepository = continentRepository;
        this.countryRepository = countryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Continent> getContinents() {
        return continentRepository.findAll();
    }

    @Override
    @Transactional
    public void saveContinent(Continent continent) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(continent)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(continentRepository, continent)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        continentRepository.saveAndFlush(continent);
    }

    @Override
    @Transactional
    public void deleteContinent(int continentId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(continentRepository, continentId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereContinentIsAForeignKey(), FK_CONTINENT.columnName, continentId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        continentRepository.deleteById(continentId);
    }

    @Override
    @Transactional
    public void updateContinent(int continentId, Continent continent)
            throws IllegalArgumentException, DataIntegrityViolationException {

        if (RepositoryHelper.cannotFindId(continentRepository, continentId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }

        Continent continentToUpdate = RepositoryHelper.getById(continentRepository, continentId);
        if (continent.getName() != null) continentToUpdate.setName(continent.getName());
        if (continent.getDescription() != null) continentToUpdate.setDescription(continent.getDescription());
    }

    private List<CrudRepository> getReposWhereContinentIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(countryRepository, eventRepository));
    }
}
