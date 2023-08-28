package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IMonth;
import com.mcommings.campaigner.models.Month;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.IMonthRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_MONTH;

@Service
public class MonthService implements IMonth {

    private final IMonthRepository monthRepository;

    @Autowired
    public MonthService(IMonthRepository monthRepository) {
        this.monthRepository = monthRepository;
    }

    @Override
    public List<Month> getMonths() {
        return monthRepository.findAll();
    }

    @Override
    @Transactional
    public void saveMonth(Month month) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(month)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(monthRepository, month)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        monthRepository.saveAndFlush(month);
    }

    @Override
    @Transactional
    public void deleteMonth(int monthId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(monthRepository, monthId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
//        TODO: uncomment out when Week, CelestialEvent, and Event is added
//        if (RepositoryHelper.isForeignKey(getReposWhereMonthIsAForeignKey(), FK_MONTH.columnName, monthId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

        monthRepository.deleteById(monthId);
    }

    @Override
    @Transactional
    public void updateMonth(int monthId, Month month) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(monthRepository, monthId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Month monthToUpdate = RepositoryHelper.getById(monthRepository, monthId);
        monthToUpdate.setName(month.getName());
        monthToUpdate.setDescription(month.getDescription());
        monthToUpdate.setSeason(month.getSeason());
    }

//        TODO: uncomment out when Week, CelestialEvent, and Event is added
//    private List<CrudRepository> getReposWhereMonthIsAForeignKey() {
//        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(
//                weekRepository, celestialEventsRepository, eventsRepository
//        ));
//        return repositories;
//    }
}