package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.calendar.dtos.CelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.mappers.CelestialEventMapper;
import com.mcommings.campaigner.modules.calendar.repositories.ICelestialEventRepository;
import com.mcommings.campaigner.modules.calendar.services.interfaces.ICelestialEvent;
import jakarta.transaction.Transactional;
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
public class CelestialEventService implements ICelestialEvent {

    private final ICelestialEventRepository celestialEventRepository;
    private final CelestialEventMapper celestialEventMapper;

    @Override
    public List<CelestialEventDTO> getCelestialEvents() {
        return celestialEventRepository.findAll()
                .stream()
                .map(celestialEventMapper::mapToCelestialEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CelestialEventDTO> getCelestialEvent(int celestialEventId) {
        return celestialEventRepository.findById(celestialEventId)
                .map(celestialEventMapper::mapToCelestialEventDto);
    }

    @Override
    public List<CelestialEventDTO> getCelestialEventsByCampaignUUID(UUID uuid) {
        return celestialEventRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(celestialEventMapper::mapToCelestialEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CelestialEventDTO> getCelestialEventsByMoon(int moonId) {
        return celestialEventRepository.findByfk_moon(moonId)
                .stream()
                .map(celestialEventMapper::mapToCelestialEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CelestialEventDTO> getCelestialEventsBySun(int sunId) {
        return celestialEventRepository.findByfk_sun(sunId)
                .stream()
                .map(celestialEventMapper::mapToCelestialEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CelestialEventDTO> getCelestialEventsByMonth(int monthId) {
        return celestialEventRepository.findByfk_month(monthId)
                .stream()
                .map(celestialEventMapper::mapToCelestialEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveCelestialEvent(CelestialEventDTO celestialEvent) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(celestialEvent)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(celestialEventRepository, celestialEvent.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        celestialEventMapper.mapToCelestialEventDto(
                celestialEventRepository.save(celestialEventMapper.mapFromCelestialEventDto(celestialEvent))
        );
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
    public Optional<CelestialEventDTO> updateCelestialEvent(int celestialEventId, CelestialEventDTO celestialEvent) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(celestialEventRepository, celestialEventId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(celestialEvent)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(celestialEventRepository, celestialEvent.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return celestialEventRepository.findById(celestialEventId).map(foundCelestialEvent -> {
            if (celestialEvent.getName() != null) foundCelestialEvent.setName(celestialEvent.getName());
            if (celestialEvent.getDescription() != null)
                foundCelestialEvent.setDescription(celestialEvent.getDescription());
            if (celestialEvent.getFk_campaign_uuid() != null)
                foundCelestialEvent.setFk_campaign_uuid(celestialEvent.getFk_campaign_uuid());
            if (celestialEvent.getFk_moon() != null) foundCelestialEvent.setFk_moon(celestialEvent.getFk_moon());
            if (celestialEvent.getFk_sun() != null) foundCelestialEvent.setFk_sun(celestialEvent.getFk_sun());
            if (celestialEvent.getFk_month() != null) foundCelestialEvent.setFk_month(celestialEvent.getFk_month());
            if (celestialEvent.getFk_week() != null) foundCelestialEvent.setFk_week(celestialEvent.getFk_week());
            if (celestialEvent.getFk_day() != null) foundCelestialEvent.setFk_day(celestialEvent.getFk_day());
            if (celestialEvent.getEvent_year() != 0) foundCelestialEvent.setEvent_year(celestialEvent.getEvent_year());

            return celestialEventMapper.mapToCelestialEventDto(celestialEventRepository.save(foundCelestialEvent));
        });
    }
    
}
