package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.interfaces.people.IEventPlacePerson;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.people.EventPlacePerson;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.locations.IPlaceRepository;
import com.mcommings.campaigner.repositories.people.IEventPlacePersonRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.EVENT_PLACE_PERSON_EXISTS;
import static com.mcommings.campaigner.enums.ErrorMessage.INSERT_FOREIGN_KEY;

@Service
public class EventPlacePersonService implements IEventPlacePerson {

    private final IEventPlacePersonRepository eventPlacePersonRepository;
    private final IEventRepository eventRepository;
    private final IPlaceRepository placeRepository;
    private final IPersonRepository personRepository;

    @Autowired
    public EventPlacePersonService(IEventPlacePersonRepository eventPlacePersonRepository,
                                   IEventRepository eventRepository, IPlaceRepository placeRepository,
                                   IPersonRepository personRepository) {
        this.eventPlacePersonRepository = eventPlacePersonRepository;
        this.eventRepository = eventRepository;
        this.placeRepository = placeRepository;
        this.personRepository = personRepository;
    }

    @Override
    public List<EventPlacePerson> getEventsPlacesPeople() {
        return eventPlacePersonRepository.findAll();
    }

    @Override
    @Transactional
    public void saveEventPlacePerson(EventPlacePerson eventPlacePerson) throws IllegalArgumentException, DataIntegrityViolationException {
        if (eventPlacePersonAlreadyExists(eventPlacePerson)) {
            throw new DataIntegrityViolationException(EVENT_PLACE_PERSON_EXISTS.message);
        }
        if (hasForeignKeys(eventPlacePerson) &&
                RepositoryHelper.foreignKeyIsNotValid(eventPlacePersonRepository, getListOfForeignKeyRepositories(), eventPlacePerson)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        eventPlacePersonRepository.saveAndFlush(eventPlacePerson);
    }

    @Override
    @Transactional
    public void deleteEventPlacePerson(int id) throws IllegalArgumentException, DataIntegrityViolationException {

    }

    @Override
    @Transactional
    public void updateEventPlacePerson(int id, EventPlacePerson eventPlacePerson) throws IllegalArgumentException, DataIntegrityViolationException {

    }

    private boolean eventPlacePersonAlreadyExists(EventPlacePerson eventPlacePerson) {
        return eventPlacePersonRepository.eventPlacePersonExists(eventPlacePerson).isPresent();
    }

    private boolean hasForeignKeys(EventPlacePerson eventPlacePerson) {
        return eventPlacePerson.getFk_event() != null ||
                eventPlacePerson.getFk_place() != null ||
                eventPlacePerson.getFk_person() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(eventRepository, placeRepository, personRepository));
    }
}
