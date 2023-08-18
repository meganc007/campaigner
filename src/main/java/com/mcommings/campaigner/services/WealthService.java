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
            throw new IllegalArgumentException("Wealth name cannot be null or empty.");
        }
        if(RepositoryHelper.nameAlreadyExists(wealthRepository, wealth)) {
            throw new DataIntegrityViolationException("Wealth already exists.");
        }

        wealthRepository.saveAndFlush(wealth);
    }

    @Override
    @Transactional
    public void deleteWealth(int wealthId) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(wealthRepository, wealthId)) {
            throw new IllegalArgumentException("Unable to delete; This wealth not be found.");
        }
        //TODO: check if foreign key

        wealthRepository.deleteById(wealthId);
    }

    @Override
    @Transactional
    public void updateWealth(int wealthId, Wealth wealth) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(wealthRepository, wealthId)) {
            throw new IllegalArgumentException("Unable to update; This wealth not be found.");
        }

        Wealth wealthToUpdate = RepositoryHelper.getById(wealthRepository, wealthId);
        wealthToUpdate.setName(wealth.getName());
    }
}
