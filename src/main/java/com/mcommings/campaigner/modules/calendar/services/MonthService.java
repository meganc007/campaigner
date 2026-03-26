package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.calendar.dtos.months.CreateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.UpdateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.ViewMonthDTO;
import com.mcommings.campaigner.modules.calendar.entities.Month;
import com.mcommings.campaigner.modules.calendar.mappers.MonthMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IMonthRepository;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MonthService extends BaseService<
        Month,
        Integer,
        ViewMonthDTO,
        CreateMonthDTO,
        UpdateMonthDTO> {

    private final IMonthRepository monthRepository;
    private final ICampaignRepository campaignRepository;
    private final MonthMapper monthMapper;

    @Override
    protected JpaRepository<Month, Integer> getRepository() {
        return monthRepository;
    }

    @Override
    protected ViewMonthDTO toViewDto(Month entity) {
        return monthMapper.toDto(entity);
    }

    @Override
    protected Month toEntity(CreateMonthDTO dto) {
        Month entity = monthMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateMonthDTO dto, Month entity) {
        monthMapper.updateMonthFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateMonthDTO dto) {
        return dto.getId();
    }

    public List<ViewMonthDTO> getMonthsByCampaignUUID(UUID uuid) {

        return query(monthRepository::findByCampaign_Uuid, uuid);
    }
}
