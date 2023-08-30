package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IWeek;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.Week;
import com.mcommings.campaigner.models.repositories.IDayRepository;
import com.mcommings.campaigner.models.repositories.IMonthRepository;
import com.mcommings.campaigner.models.repositories.IWeekRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_WEEK;

@Service
public class WeekService implements IWeek {

    private final IWeekRepository weekRepository;
    private final IDayRepository dayRepository;
    private final IMonthRepository monthRepository;

    @Autowired
    public WeekService(IWeekRepository weekRepository, IDayRepository dayRepository, IMonthRepository monthRepository) {
        this.weekRepository = weekRepository;
        this.dayRepository = dayRepository;
        this.monthRepository = monthRepository;
    }

    @Override
    public List<Week> getWeeks() {
        return weekRepository.findAll();
    }

    @Override
    @Transactional
    public void saveWeek(Week week) throws DataIntegrityViolationException {
        if (RepositoryHelper.foreignKeyIsNotValid(weekRepository, getListOfForeignKeyRepositories(), week)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        weekRepository.saveAndFlush(week);
    }

    @Override
    @Transactional
    public void deleteWeek(int weekId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weekRepository, weekId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereWeekIsAForeignKey(), FK_WEEK.columnName, weekId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        weekRepository.deleteById(weekId);
    }

    @Override
    @Transactional
    public void updateWeek(int weekId, Week week) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weekRepository, weekId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.foreignKeyIsNotValid(weekRepository, getListOfForeignKeyRepositories(), week)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        Week weekToUpdate = RepositoryHelper.getById(weekRepository, weekId);
        weekToUpdate.setDescription(week.getDescription());
        weekToUpdate.setWeek_number(week.getWeek_number());
        weekToUpdate.setFk_month(week.getFk_month());
    }

    private List<CrudRepository> getReposWhereWeekIsAForeignKey() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(dayRepository));
        return repositories;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(monthRepository));
        return repositories;
    }
}
