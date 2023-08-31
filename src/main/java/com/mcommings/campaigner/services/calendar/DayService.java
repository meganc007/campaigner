package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.interfaces.calendar.IDay;
import com.mcommings.campaigner.models.calendar.Day;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.calendar.IDayRepository;
import com.mcommings.campaigner.models.repositories.calendar.IWeekRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class DayService implements IDay {

    private final IDayRepository dayRepository;
    private final IWeekRepository weekRepository;

    @Autowired
    public DayService(IDayRepository dayRepository, IWeekRepository weekRepository) {
        this.dayRepository = dayRepository;
        this.weekRepository = weekRepository;
    }

    @Override
    public List<Day> getDays() {
        return dayRepository.findAll();
    }

    @Override
    @Transactional
    public void saveDay(Day day) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(day)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.foreignKeyIsNotValid(dayRepository, getListOfForeignKeyRepositories(), day)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        dayRepository.saveAndFlush(day);
    }

    @Override
    @Transactional
    public void deleteDay(int dayId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(dayRepository, dayId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: check if Day is fk before deleting
        dayRepository.deleteById(dayId);
    }

    @Override
    @Transactional
    public void updateDay(int dayId, Day day) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(dayRepository, dayId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.foreignKeyIsNotValid(dayRepository, getListOfForeignKeyRepositories(), day)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        Day dayToUpdate = RepositoryHelper.getById(dayRepository, dayId);
        dayToUpdate.setName(day.getName());
        dayToUpdate.setDescription(day.getDescription());
        dayToUpdate.setFk_week(day.getFk_week());
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(weekRepository));
        return repositories;
    }
}
