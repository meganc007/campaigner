package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.calendar.dtos.events.CreateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.UpdateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.ViewEventDTO;
import com.mcommings.campaigner.modules.calendar.entities.Event;
import com.mcommings.campaigner.modules.calendar.mappers.EventMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IEventRepository;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService extends BaseService<
        Event,
        Integer,
        ViewEventDTO,
        CreateEventDTO,
        UpdateEventDTO> {

    private final IEventRepository eventRepository;
    private final ICampaignRepository campaignRepository;
    private final EventMapper eventMapper;

    @Override
    protected JpaRepository<Event, Integer> getRepository() {
        return eventRepository;
    }

    @Override
    protected ViewEventDTO toViewDto(Event entity) {
        return eventMapper.toDto(entity);
    }

    @Override
    protected Event toEntity(CreateEventDTO dto) {
        Event entity = eventMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateEventDTO dto, Event entity) {
        eventMapper.updateEventFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateEventDTO dto) {
        return dto.getId();
    }

    public List<ViewEventDTO> getEventsByCampaignUUID(UUID uuid) {

        return query(eventRepository::findByCampaign_Uuid, uuid);
    }

    public List<ViewEventDTO> getEventsByYear(int year) {

        return query(eventRepository::findByYear_Id, year);
    }

    public List<ViewEventDTO> getEventsByMonthId(int monthId) {

        return query(eventRepository::findByMonth_Id, monthId);
    }

    public List<ViewEventDTO> getEventsByWeekId(int weekId) {

        return query(eventRepository::findByWeek_Id, weekId);
    }

    public List<ViewEventDTO> getEventsByDayId(int dayId) {

        return query(eventRepository::findByDay_Id, dayId);
    }

    public List<ViewEventDTO> getEventsByContinentId(int continentId) {

        return query(eventRepository::findByContinent_Id, continentId);
    }

    public List<ViewEventDTO> getEventsByCountryId(int countryId) {

        return query(eventRepository::findByCountry_Id, countryId);
    }

    public List<ViewEventDTO> getEventsByCityId(int cityId) {

        return query(eventRepository::findByCity_Id, cityId);
    }
}
