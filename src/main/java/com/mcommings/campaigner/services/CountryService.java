package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.ICountry;
import com.mcommings.campaigner.models.Country;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.ICountryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.CSS;
import java.util.List;

import static com.mcommings.campaigner.ErrorMessage.NAME_EXISTS;
import static com.mcommings.campaigner.ErrorMessage.NULL_OR_EMPTY;

@Service
public class CountryService implements ICountry {

    private final ICountryRepository countryRepository;

    @Autowired
    public CountryService(ICountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getCountries() {
        return countryRepository.findAll();
    }

    @Override
    @Transactional
    public void saveCountry(Country country) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.nameIsNullOrEmpty(country)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if(RepositoryHelper.nameAlreadyExists(countryRepository, country)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        //TODO: if wrong # of FKs present throw IllegalArgumentException
//        countryRepository.saveAndFlush(job);

    }

    @Override
    @Transactional
    public void deleteCountry(int countryId) throws IllegalArgumentException, DataIntegrityViolationException {

    }

    @Override
    @Transactional
    public void updateCountry(int countryId, Country country) throws IllegalArgumentException, DataIntegrityViolationException {

    }
}
