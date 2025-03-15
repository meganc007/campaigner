package com.mcommings.campaigner.common.services;

import com.mcommings.campaigner.common.entities.RepositoryHelper;
import com.mcommings.campaigner.common.entities.Wealth;
import com.mcommings.campaigner.common.repositories.IWealthRepository;
import com.mcommings.campaigner.common.services.interfaces.IWealth;
import com.mcommings.campaigner.locations.repositories.ICityRepository;
import com.mcommings.campaigner.people.repositories.INamedMonsterRepository;
import com.mcommings.campaigner.people.repositories.IPersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_WEALTH;

@Service
public class WealthService implements IWealth {

    private final IWealthRepository wealthRepository;
    private final ICityRepository cityRepository;
    private final IPersonRepository personRepository;
    private final INamedMonsterRepository namedMonsterRepository;

    @Autowired
    public WealthService(IWealthRepository wealthRepository, ICityRepository cityRepository,
                         IPersonRepository personRepository, INamedMonsterRepository namedMonsterRepository) {
        this.wealthRepository = wealthRepository;
        this.cityRepository = cityRepository;
        this.personRepository = personRepository;
        this.namedMonsterRepository = namedMonsterRepository;
    }

    @Override
    public List<Wealth> getWealth() {
        return wealthRepository.findAll();
    }

    @Override
    @Transactional
    public void saveWealth(Wealth wealth) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(wealth)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(wealthRepository, wealth)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        wealthRepository.saveAndFlush(wealth);
    }

    @Override
    @Transactional
    public void deleteWealth(int wealthId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(wealthRepository, wealthId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereWealthIsAForeignKey(), FK_WEALTH.columnName, wealthId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        wealthRepository.deleteById(wealthId);
    }

    @Override
    @Transactional
    public void updateWealth(int wealthId, Wealth wealth) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(wealthRepository, wealthId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }

        Wealth wealthToUpdate = RepositoryHelper.getById(wealthRepository, wealthId);
        wealthToUpdate.setName(wealth.getName());
    }

    private List<CrudRepository> getReposWhereWealthIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(cityRepository, personRepository, namedMonsterRepository));
    }
}
