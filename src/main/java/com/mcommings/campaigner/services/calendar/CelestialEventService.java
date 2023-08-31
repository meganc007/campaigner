package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.interfaces.calendar.ICelestialEvent;
import com.mcommings.campaigner.interfaces.calendar.IMoon;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.calendar.CelestialEvent;
import com.mcommings.campaigner.models.repositories.calendar.*;
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
public class CelestialEventService implements ICelestialEvent {

    private final ICelestialEventRepository celestialEventRepository;
    private final IMoonRepository moonRepository;
    private final ISunRepository sunRepository;
    private final IMonthRepository monthRepository;
    private final IWeekRepository weekRepository;
    private final IDayRepository dayRepository;

    @Autowired
    public CelestialEventService(ICelestialEventRepository celestialEventRepository, IMoonRepository moonRepository,
                                 ISunRepository sunRepository, IMonthRepository monthRepository,
                                 IWeekRepository weekRepository, IDayRepository dayRepository) {
        this.celestialEventRepository = celestialEventRepository;
        this.moonRepository = moonRepository;
        this.sunRepository = sunRepository;
        this.monthRepository = monthRepository;
        this.weekRepository = weekRepository;
        this.dayRepository = dayRepository;
    }

    @Override
    public List<CelestialEvent> getCelestialEvents() {
        return celestialEventRepository.findAll();
    }

    @Override
    @Transactional
    public void saveCelestialEvent(CelestialEvent celestialEvent) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(celestialEvent)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (hasForeignKeys(celestialEvent) &&
                RepositoryHelper.foreignKeyIsNotValid(celestialEventRepository, getListOfForeignKeyRepositories(), celestialEvent)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        celestialEventRepository.saveAndFlush(celestialEvent);
    }

    @Override
    @Transactional
    public void deleteCelestialEvent(int celestialEventId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(celestialEventRepository, celestialEventId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        celestialEventRepository.deleteById(celestialEventId);
    }

    @Override
    @Transactional
    public void updateCelestialEvent(int celestialEventId, CelestialEvent celestialEvent) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(celestialEventRepository, celestialEventId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(celestialEvent) &&
                RepositoryHelper.foreignKeyIsNotValid(celestialEventRepository, getListOfForeignKeyRepositories(), celestialEvent)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        
        CelestialEvent celestialEventToUpdate = RepositoryHelper.getById(celestialEventRepository, celestialEventId);
        celestialEventToUpdate.setName(celestialEvent.getName());
        celestialEventToUpdate.setDescription(celestialEvent.getDescription());
        celestialEventToUpdate.setFk_moon(celestialEvent.getFk_moon());
        celestialEventToUpdate.setFk_sun(celestialEvent.getFk_sun());
        celestialEventToUpdate.setFk_month(celestialEvent.getFk_month());
        celestialEventToUpdate.setFk_week(celestialEvent.getFk_week());
        celestialEventToUpdate.setFk_day(celestialEvent.getFk_day());
        celestialEventToUpdate.setEvent_year(celestialEvent.getEvent_year());
    }

    private boolean hasForeignKeys(CelestialEvent celestialEvent) {
        return celestialEvent.getFk_moon() != null ||
                celestialEvent.getFk_sun() != null ||
                celestialEvent.getFk_month() != null ||
                celestialEvent.getFk_week() != null ||
                celestialEvent.getFk_day() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(moonRepository, sunRepository, monthRepository, weekRepository, dayRepository));
        return repositories;
    }
}
