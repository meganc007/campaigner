package com.mcommings.campaigner.calendar.services;

import com.mcommings.campaigner.calendar.entities.Week;
import com.mcommings.campaigner.calendar.repositories.ICelestialEventRepository;
import com.mcommings.campaigner.calendar.repositories.IDayRepository;
import com.mcommings.campaigner.calendar.repositories.IMonthRepository;
import com.mcommings.campaigner.calendar.repositories.IWeekRepository;
import com.mcommings.campaigner.calendar.services.interfaces.IWeek;
import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.repositories.IEventRepository;
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
import static com.mcommings.campaigner.enums.ForeignKey.FK_WEEK;

@Service
public class WeekService implements IWeek {

    private final IWeekRepository weekRepository;
    private final IDayRepository dayRepository;
    private final IMonthRepository monthRepository;
    private final ICelestialEventRepository celestialEventRepository;
    private final IEventRepository eventRepository;

    @Autowired
    public WeekService(IWeekRepository weekRepository, IDayRepository dayRepository, IMonthRepository monthRepository,
                       ICelestialEventRepository celestialEventRepository, IEventRepository eventRepository) {
        this.weekRepository = weekRepository;
        this.dayRepository = dayRepository;
        this.monthRepository = monthRepository;
        this.celestialEventRepository = celestialEventRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Week> getWeeks() {
        return weekRepository.findAll();
    }

    @Override
    public List<Week> getWeeksByCampaignUUID(UUID uuid) {
        return weekRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    public List<Week> getWeeksByMonth(int monthId) {
        return weekRepository.findByfk_month(monthId);
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
        if (week.getDescription() != null) weekToUpdate.setDescription(week.getDescription());
        if (week.getWeek_number() != null) weekToUpdate.setWeek_number(week.getWeek_number());
        if (week.getFk_month() != null) weekToUpdate.setFk_month(week.getFk_month());
    }

    private List<CrudRepository> getReposWhereWeekIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(dayRepository, celestialEventRepository, eventRepository));
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(monthRepository));
    }
}
