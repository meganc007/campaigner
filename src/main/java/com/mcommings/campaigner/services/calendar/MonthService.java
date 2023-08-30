package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.interfaces.calendar.IMonth;
import com.mcommings.campaigner.models.calendar.Month;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.calendar.IMonthRepository;
import com.mcommings.campaigner.models.repositories.calendar.IWeekRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class MonthService implements IMonth {

    private final IMonthRepository monthRepository;
    private final IWeekRepository weekRepository;

    @Autowired
    public MonthService(IMonthRepository monthRepository, IWeekRepository weekRepository) {
        this.monthRepository = monthRepository;
        this.weekRepository= weekRepository;
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
//        TODO: uncomment out when CelestialEvent and Event is added
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
