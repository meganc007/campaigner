package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.calendar.dtos.moons.CreateMoonDTO;
import com.mcommings.campaigner.modules.calendar.dtos.moons.UpdateMoonDTO;
import com.mcommings.campaigner.modules.calendar.dtos.moons.ViewMoonDTO;
import com.mcommings.campaigner.modules.calendar.entities.Moon;
import com.mcommings.campaigner.modules.calendar.mappers.MoonMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IMoonRepository;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoonService extends BaseService<
        Moon,
        Integer,
        ViewMoonDTO,
        CreateMoonDTO,
        UpdateMoonDTO> {

    private final IMoonRepository moonRepository;
    private final ICampaignRepository campaignRepository;
    private final MoonMapper moonMapper;

    @Override
    protected JpaRepository<Moon, Integer> getRepository() {
        return moonRepository;
    }

    @Override
    protected ViewMoonDTO toViewDto(Moon entity) {
        return moonMapper.toDto(entity);
    }

    @Override
    protected Moon toEntity(CreateMoonDTO dto) {
        Moon entity = moonMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateMoonDTO dto, Moon entity) {
        moonMapper.updateMoonFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateMoonDTO dto) {
        return dto.getId();
    }

    public List<ViewMoonDTO> getMoonsByCampaignUUID(UUID uuid) {

        return query(moonRepository::findByCampaign_Uuid, uuid);
    }

}
