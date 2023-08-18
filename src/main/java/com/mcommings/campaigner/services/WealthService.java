package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IWealth;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.Wealth;
import com.mcommings.campaigner.models.repositories.IWealthRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.ErrorMessage.*;

@Service
public class WealthService implements IWealth {

    private final IWealthRepository wealthRepository;

    @Autowired
    public WealthService(IWealthRepository wealthRepository) {this.wealthRepository = wealthRepository;}
    @Override
    public List<Wealth> getWealth() {
        return wealthRepository.findAll();
    }

    @Override
    @Transactional
    public void saveWealth(Wealth wealth) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.nameIsNullOrEmpty(wealth)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if(RepositoryHelper.nameAlreadyExists(wealthRepository, wealth)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        wealthRepository.saveAndFlush(wealth);
    }

    @Override
    @Transactional
    public void deleteWealth(int wealthId) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(wealthRepository, wealthId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: check if foreign key

        wealthRepository.deleteById(wealthId);
    }

    @Override
    @Transactional
    public void updateWealth(int wealthId, Wealth wealth) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(wealthRepository, wealthId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }

        Wealth wealthToUpdate = RepositoryHelper.getById(wealthRepository, wealthId);
        wealthToUpdate.setName(wealth.getName());
    }
}
