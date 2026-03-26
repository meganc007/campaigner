package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.calendar.dtos.suns.CreateSunDTO;
import com.mcommings.campaigner.modules.calendar.dtos.suns.UpdateSunDTO;
import com.mcommings.campaigner.modules.calendar.dtos.suns.ViewSunDTO;
import com.mcommings.campaigner.modules.calendar.entities.Sun;
import com.mcommings.campaigner.modules.calendar.mappers.SunMapper;
import com.mcommings.campaigner.modules.calendar.repositories.ISunRepository;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SunService extends BaseService<
        Sun,
        Integer,
        ViewSunDTO,
        CreateSunDTO,
        UpdateSunDTO> {

    private final ISunRepository sunRepository;
    private final ICampaignRepository campaignRepository;
    private final SunMapper sunMapper;

    @Override
    protected JpaRepository<Sun, Integer> getRepository() {
        return sunRepository;
    }

    @Override
    protected ViewSunDTO toViewDto(Sun entity) {
        return sunMapper.toDto(entity);
    }

    @Override
    protected Sun toEntity(CreateSunDTO dto) {
        Sun entity = sunMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateSunDTO dto, Sun entity) {
        sunMapper.updateSunFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateSunDTO dto) {
        return dto.getId();
    }

    public List<ViewSunDTO> getSunsByCampaignUUID(UUID uuid) {

        return query(sunRepository::findByCampaign_Uuid, uuid);
    }

}
