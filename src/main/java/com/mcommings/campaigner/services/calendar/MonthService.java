package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.interfaces.calendar.IMonth;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.calendar.Month;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.calendar.ICelestialEventRepository;
import com.mcommings.campaigner.repositories.calendar.IMonthRepository;
import com.mcommings.campaigner.repositories.calendar.IWeekRepository;
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
import static com.mcommings.campaigner.enums.ForeignKey.FK_MONTH;

@Service
public class MonthService implements IMonth {

    private final IMonthRepository monthRepository;
    private final IWeekRepository weekRepository;
    private final ICelestialEventRepository celestialEventRepository;
    private final IEventRepository eventRepository;

    @Autowired
    public MonthService(IMonthRepository monthRepository, IWeekRepository weekRepository,
                        ICelestialEventRepository celestialEventRepository, IEventRepository eventRepository) {
        this.monthRepository = monthRepository;
        this.weekRepository = weekRepository;
        this.celestialEventRepository = celestialEventRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Month> getMonths() {
        return monthRepository.findAll();
    }

    @Override
    public List<Month> getMonthsByCampaignUUID(UUID uuid) {
        return monthRepository.findByfk_campaign_uuid(uuid);
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
        if (RepositoryHelper.isForeignKey(getReposWhereMonthIsAForeignKey(), FK_MONTH.columnName, monthId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        monthRepository.deleteById(monthId);
    }

    @Override
    @Transactional
    public void updateMonth(int monthId, Month month) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(monthRepository, monthId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Month monthToUpdate = RepositoryHelper.getById(monthRepository, monthId);
        if (month.getName() != null) monthToUpdate.setName(month.getName());
        if (month.getDescription() != null) monthToUpdate.setDescription(month.getDescription());
        if (month.getSeason() != null) monthToUpdate.setSeason(month.getSeason());
    }

    private List<CrudRepository> getReposWhereMonthIsAForeignKey() {
       return new ArrayList<>(Arrays.asList(weekRepository, celestialEventRepository, eventRepository));
    }
}
