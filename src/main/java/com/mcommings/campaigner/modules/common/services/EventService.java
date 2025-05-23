package com.mcommings.campaigner.modules.common.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.common.dtos.EventDTO;
import com.mcommings.campaigner.modules.common.mappers.EventMapper;
import com.mcommings.campaigner.modules.common.repositories.IEventRepository;
import com.mcommings.campaigner.modules.common.services.interfaces.IEvent;
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
public class EventService implements IEvent {

    private final IEventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public List<EventDTO> getEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::mapToEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EventDTO> getEvent(int eventId) {
        return eventRepository.findById(eventId)
                .map(eventMapper::mapToEventDto);
    }

    @Override
    public List<EventDTO> getEventsByCampaignUUID(UUID uuid) {
        return eventRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(eventMapper::mapToEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByYear(int year) {
        return eventRepository.findByEventYear(year)
                .stream()
                .map(eventMapper::mapToEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByMonth(int monthId) {
        return eventRepository.findByfk_month(monthId)
                .stream()
                .map(eventMapper::mapToEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByWeek(int weekId) {
        return eventRepository.findByfk_week(weekId)
                .stream()
                .map(eventMapper::mapToEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByDay(int dayId) {
        return eventRepository.findByfk_day(dayId)
                .stream()
                .map(eventMapper::mapToEventDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<EventDTO> getEventsByContinent(int continentId) {
        return eventRepository.findByfk_continent(continentId)
                .stream()
                .map(eventMapper::mapToEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByCountry(int countryId) {
        return eventRepository.findByfk_country(countryId)
                .stream()
                .map(eventMapper::mapToEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByCity(int cityId) {

        return eventRepository.findByfk_city(cityId)
                .stream()
                .map(eventMapper::mapToEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveEvent(EventDTO event) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(event)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(eventRepository, event.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        eventMapper.mapToEventDto(
                eventRepository.save(
                        eventMapper.mapFromEventDto(event))
        );
    }

    @Override
    public void deleteEvent(int eventId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(eventRepository, eventId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        eventRepository.deleteById(eventId);
    }

    @Override
    public Optional<EventDTO> updateEvent(int eventId, EventDTO event) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(eventRepository, eventId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(event)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(eventRepository, event.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return eventRepository.findById(eventId).map(foundEvent -> {
            if (event.getName() != null) foundEvent.setName(event.getName());
            if (event.getDescription() != null) foundEvent.setDescription(event.getDescription());
            if (event.getEventYear() != 0) foundEvent.setEventYear(event.getEventYear());
            if (event.getFk_month() != null) foundEvent.setFk_month(event.getFk_month());
            if (event.getFk_week() != null) foundEvent.setFk_week(event.getFk_week());
            if (event.getFk_day() != null) foundEvent.setFk_day(event.getFk_day());
            if (event.getFk_city() != null) foundEvent.setFk_city(event.getFk_city());
            if (event.getFk_continent() != null) foundEvent.setFk_continent(event.getFk_continent());
            if (event.getFk_country() != null) foundEvent.setFk_country(event.getFk_country());

            return eventMapper.mapToEventDto(eventRepository.save(foundEvent));
        });
    }
}
