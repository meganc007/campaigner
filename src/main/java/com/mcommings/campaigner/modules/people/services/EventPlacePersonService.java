package com.mcommings.campaigner.modules.people.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.people.dtos.EventPlacePersonDTO;
import com.mcommings.campaigner.modules.people.entities.EventPlacePerson;
import com.mcommings.campaigner.modules.people.mappers.EventPlacePersonMapper;
import com.mcommings.campaigner.modules.people.repositories.IEventPlacePersonRepository;
import com.mcommings.campaigner.modules.people.services.interfaces.IEventPlacePerson;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class EventPlacePersonService implements IEventPlacePerson {

    private final IEventPlacePersonRepository eventPlacePersonRepository;
    private final EventPlacePersonMapper eventPlacePersonMapper;

    @Override
    public List<EventPlacePersonDTO> getEventsPlacesPeople() {

        return eventPlacePersonRepository.findAll()
                .stream()
                .map(eventPlacePersonMapper::mapToEventPlacePersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EventPlacePersonDTO> getEventPlacePerson(int eventPlacePersonId) {
        return eventPlacePersonRepository.findById(eventPlacePersonId)
                .map(eventPlacePersonMapper::mapToEventPlacePersonDto);
    }

    @Override
    public List<EventPlacePersonDTO> getEventsPlacesPeopleByCampaignUUID(UUID uuid) {
        return eventPlacePersonRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(eventPlacePersonMapper::mapToEventPlacePersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventPlacePersonDTO> getEventsPlacesPeopleByPerson(int personId) {
        return eventPlacePersonRepository.findByfk_person(personId)
                .stream()
                .map(eventPlacePersonMapper::mapToEventPlacePersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventPlacePersonDTO> getEventsPlacesPeopleByPlace(int placeId) {
        return eventPlacePersonRepository.findByfk_place(placeId)
                .stream()
                .map(eventPlacePersonMapper::mapToEventPlacePersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventPlacePersonDTO> getEventsPlacesPeopleByEvent(int eventId) {
        return eventPlacePersonRepository.findByfk_event(eventId)
                .stream()
                .map(eventPlacePersonMapper::mapToEventPlacePersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveEventPlacePerson(EventPlacePersonDTO eventPlacePerson) throws IllegalArgumentException, DataIntegrityViolationException {
        if (eventPlacePersonAlreadyExists(eventPlacePersonMapper.mapFromEventPlacePersonDto(eventPlacePerson))) {
            throw new DataIntegrityViolationException(EVENT_PLACE_PERSON_EXISTS.message);
        }

        eventPlacePersonMapper.mapToEventPlacePersonDto(
                eventPlacePersonRepository.save(eventPlacePersonMapper.mapFromEventPlacePersonDto(eventPlacePerson))
        );
    }

    @Override
    public void deleteEventPlacePerson(int id) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(eventPlacePersonRepository, id)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        eventPlacePersonRepository.deleteById(id);
    }

    @Override
    public Optional<EventPlacePersonDTO> updateEventPlacePerson(int id, EventPlacePersonDTO eventPlacePerson) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(eventPlacePersonRepository, id)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }

        return eventPlacePersonRepository.findById(id).map(foundEventPlacePerson -> {
            if (eventPlacePerson.getFk_event() != null)
                foundEventPlacePerson.setFk_event(eventPlacePerson.getFk_event());
            if (eventPlacePerson.getFk_place() != null)
                foundEventPlacePerson.setFk_place(eventPlacePerson.getFk_place());
            if (eventPlacePerson.getFk_person() != null)
                foundEventPlacePerson.setFk_person(eventPlacePerson.getFk_person());

            return eventPlacePersonMapper.mapToEventPlacePersonDto(eventPlacePersonRepository.save(foundEventPlacePerson));
        });
    }

    private boolean eventPlacePersonAlreadyExists(EventPlacePerson eventPlacePerson) {
        return eventPlacePersonRepository.eventPlacePersonExists(eventPlacePerson).isPresent();
    }
}
