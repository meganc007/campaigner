package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import com.mcommings.campaigner.modules.calendar.repositories.*;
import com.mcommings.campaigner.modules.calendar.services.interfaces.ICelestialEvent;
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
    public List<CelestialEvent> getCelestialEventsByCampaignUUID(UUID uuid) {
        return celestialEventRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    public List<CelestialEvent> getCelestialEventsByMoon(int moonId) {
        return celestialEventRepository.findByfk_moon(moonId);
    }

    @Override
    public List<CelestialEvent> getCelestialEventsBySun(int sunId) {
        return celestialEventRepository.findByfk_sun(sunId);
    }

    @Override
    public List<CelestialEvent> getCelestialEventsByMonth(int monthId) {
        return celestialEventRepository.findByfk_month(monthId);
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
        if (celestialEvent.getName() != null) celestialEventToUpdate.setName(celestialEvent.getName());
        if (celestialEvent.getDescription() != null)
            celestialEventToUpdate.setDescription(celestialEvent.getDescription());
        if (celestialEvent.getFk_moon() != null) celestialEventToUpdate.setFk_moon(celestialEvent.getFk_moon());
        if (celestialEvent.getFk_sun() != null) celestialEventToUpdate.setFk_sun(celestialEvent.getFk_sun());
        if (celestialEvent.getFk_month() != null) celestialEventToUpdate.setFk_month(celestialEvent.getFk_month());
        if (celestialEvent.getFk_week() != null) celestialEventToUpdate.setFk_week(celestialEvent.getFk_week());
        if (celestialEvent.getFk_day() != null) celestialEventToUpdate.setFk_day(celestialEvent.getFk_day());
        if (celestialEvent.getEvent_year() != 0) celestialEventToUpdate.setEvent_year(celestialEvent.getEvent_year());
    }

    private boolean hasForeignKeys(CelestialEvent celestialEvent) {
        return celestialEvent.getFk_moon() != null ||
                celestialEvent.getFk_sun() != null ||
                celestialEvent.getFk_month() != null ||
                celestialEvent.getFk_week() != null ||
                celestialEvent.getFk_day() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(moonRepository, sunRepository, monthRepository, weekRepository, dayRepository));
    }
}
