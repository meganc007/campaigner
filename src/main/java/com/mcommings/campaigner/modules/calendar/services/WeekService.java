package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.CreateWeekDTO;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.UpdateWeekDTO;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.ViewWeekDTO;
import com.mcommings.campaigner.modules.calendar.entities.Week;
import com.mcommings.campaigner.modules.calendar.mappers.WeekMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IWeekRepository;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WeekService extends BaseService<
        Week,
        Integer,
        ViewWeekDTO,
        CreateWeekDTO,
        UpdateWeekDTO> {

    private final IWeekRepository weekRepository;
    private final ICampaignRepository campaignRepository;
    private final WeekMapper weekMapper;

    @Override
    protected JpaRepository<Week, Integer> getRepository() {
        return weekRepository;
    }

    @Override
    protected ViewWeekDTO toViewDto(Week entity) {
        return weekMapper.toDto(entity);
    }

    @Override
    protected Week toEntity(CreateWeekDTO dto) {
        Week entity = weekMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateWeekDTO dto, Week entity) {
        weekMapper.updateWeekFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateWeekDTO dto) {
        return dto.getId();
    }

    public List<ViewWeekDTO> getWeeksByCampaignUUID(UUID uuid) {
        return query(weekRepository::findByCampaign_Uuid, uuid);
    }

    public List<ViewWeekDTO> getWeeksByMonthId(int monthId) {
        return query(weekRepository::findByMonth_Id, monthId);
    }
}
