package com.mcommings.campaigner.calendar.services;

import com.mcommings.campaigner.calendar.entities.Day;
import com.mcommings.campaigner.calendar.repositories.ICelestialEventRepository;
import com.mcommings.campaigner.calendar.repositories.IDayRepository;
import com.mcommings.campaigner.calendar.repositories.IWeekRepository;
import com.mcommings.campaigner.calendar.services.interfaces.IDay;
import com.mcommings.campaigner.common.entities.RepositoryHelper;
import com.mcommings.campaigner.common.repositories.IEventRepository;
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
import static com.mcommings.campaigner.enums.ForeignKey.FK_DAY;

@Service
public class DayService implements IDay {

    private final IDayRepository dayRepository;
    private final IWeekRepository weekRepository;
    private final ICelestialEventRepository celestialEventRepository;
    private final IEventRepository eventRepository;

    @Autowired
    public DayService(IDayRepository dayRepository, IWeekRepository weekRepository,
                      ICelestialEventRepository celestialEventRepository, IEventRepository eventRepository) {
        this.dayRepository = dayRepository;
        this.weekRepository = weekRepository;
        this.celestialEventRepository = celestialEventRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Day> getDays() {
        return dayRepository.findAll();
    }

    @Override
    public List<Day> getDaysByCampaignUUID(UUID uuid) {
        return dayRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    public List<Day> getDaysByWeek(int weekId) {
        return dayRepository.findByfk_week(weekId);
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
        if (RepositoryHelper.isForeignKey(getReposWhereDayIsAForeignKey(), FK_DAY.columnName, dayId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }
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
        if (day.getName() != null) dayToUpdate.setName(day.getName());
        if (day.getDescription() != null) dayToUpdate.setDescription(day.getDescription());
        if (day.getFk_week() != null) dayToUpdate.setFk_week(day.getFk_week());
    }

    private List<CrudRepository> getReposWhereDayIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(celestialEventRepository, eventRepository));
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(weekRepository));
    }
}
