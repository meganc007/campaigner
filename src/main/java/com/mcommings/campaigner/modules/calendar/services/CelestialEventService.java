package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.CreateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.UpdateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.ViewCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import com.mcommings.campaigner.modules.calendar.mappers.CelestialEventMapper;
import com.mcommings.campaigner.modules.calendar.repositories.ICelestialEventRepository;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CelestialEventService extends BaseService<
        CelestialEvent,
        Integer,
        ViewCelestialEventDTO,
        CreateCelestialEventDTO,
        UpdateCelestialEventDTO> {

    private final ICelestialEventRepository celestialEventRepository;
    private final ICampaignRepository campaignRepository;
    private final CelestialEventMapper celestialEventMapper;

    @Override
    protected JpaRepository<CelestialEvent, Integer> getRepository() {
        return celestialEventRepository;
    }

    @Override
    protected ViewCelestialEventDTO toViewDto(CelestialEvent entity) {
        return celestialEventMapper.toDto(entity);
    }

    @Override
    protected CelestialEvent toEntity(CreateCelestialEventDTO dto) {
        CelestialEvent entity = celestialEventMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateCelestialEventDTO dto, CelestialEvent entity) {
        celestialEventMapper.updateCelestialEventFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateCelestialEventDTO dto) {
        return dto.getId();
    }

    public List<ViewCelestialEventDTO> getCelestialEventsByCampaignUUID(UUID uuid) {
        return query(celestialEventRepository::findByCampaign_Uuid, uuid);
    }

    public List<ViewCelestialEventDTO> getCelestialEventsByMoonId(int moonId) {
        return query(celestialEventRepository::findByMoon_Id, moonId);
    }

    public List<ViewCelestialEventDTO> getCelestialEventsBySunId(int sunId) {
        return query(celestialEventRepository::findBySun_Id, sunId);
    }

    public List<ViewCelestialEventDTO> getCelestialEventsByMonthId(int monthId) {
        return query(celestialEventRepository::findByMonth_Id, monthId);
    }

    public List<ViewCelestialEventDTO> getCelestialEventsByYearId(int yearId) {
        return query(celestialEventRepository::findByYear_Id, yearId);
    }
}
