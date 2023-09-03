package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IEvent;
import com.mcommings.campaigner.models.Event;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.calendar.IDayRepository;
import com.mcommings.campaigner.repositories.calendar.IMonthRepository;
import com.mcommings.campaigner.repositories.calendar.IWeekRepository;
import com.mcommings.campaigner.repositories.locations.ICityRepository;
import com.mcommings.campaigner.repositories.locations.IContinentRepository;
import com.mcommings.campaigner.repositories.locations.ICountryRepository;
import com.mcommings.campaigner.repositories.people.IEventPlacePersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_EVENT;

@Service
public class EventService implements IEvent {

    private final IEventRepository eventRepository;
    private final IMonthRepository monthRepository;
    private final IWeekRepository weekRepository;
    private final IDayRepository dayRepository;
    private final ICityRepository cityRepository;
    private final IContinentRepository continentRepository;
    private final ICountryRepository countryRepository;
    private final IEventPlacePersonRepository eventPlacePersonRepository;

    @Autowired
    public EventService(IEventRepository eventRepository, IMonthRepository monthRepository,
                        IWeekRepository weekRepository, IDayRepository dayRepository, ICityRepository cityRepository,
                        IContinentRepository continentRepository, ICountryRepository countryRepository,
                        IEventPlacePersonRepository eventPlacePersonRepository) {
        this.eventRepository = eventRepository;
        this.monthRepository = monthRepository;
        this.weekRepository = weekRepository;
        this.dayRepository = dayRepository;
        this.cityRepository = cityRepository;
        this.continentRepository = continentRepository;
        this.countryRepository = countryRepository;
        this.eventPlacePersonRepository = eventPlacePersonRepository;
    }

    @Override
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    @Override
    @Transactional
    public void saveEvent(Event event) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(event)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(eventRepository, event)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        if (hasForeignKeys(event) &&
                RepositoryHelper.foreignKeyIsNotValid(eventRepository, getListOfForeignKeyRepositories(), event)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        eventRepository.saveAndFlush(event);
    }

    @Override
    @Transactional
    public void deleteEvent(int eventId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(eventRepository, eventId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereEventIsAForeignKey(), FK_EVENT.columnName, eventId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }
        eventRepository.deleteById(eventId);
    }

    @Override
    @Transactional
    public void updateEvent(int eventId, Event event) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(eventRepository, eventId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(event) &&
                RepositoryHelper.foreignKeyIsNotValid(eventRepository, getListOfForeignKeyRepositories(), event)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        Event eventToUpdate = RepositoryHelper.getById(eventRepository, eventId);
        eventToUpdate.setName(event.getName());
        eventToUpdate.setDescription(event.getDescription());
        eventToUpdate.setEvent_year(event.getEvent_year());
        eventToUpdate.setFk_month(event.getFk_month());
        eventToUpdate.setFk_week(event.getFk_week());
        eventToUpdate.setFk_day(event.getFk_day());
        eventToUpdate.setFk_city(event.getFk_city());
        eventToUpdate.setFk_continent(event.getFk_continent());
        eventToUpdate.setFk_country(event.getFk_country());
    }

    private List<CrudRepository> getReposWhereEventIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(eventPlacePersonRepository));
    }

    private boolean hasForeignKeys(Event event) {
        return event.getFk_month() != null ||
                event.getFk_week() != null ||
                event.getFk_day() != null ||
                event.getFk_city() != null ||
                event.getFk_continent() != null ||
                event.getFk_country() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(monthRepository, weekRepository, dayRepository, cityRepository,
                continentRepository, countryRepository));
    }
}
