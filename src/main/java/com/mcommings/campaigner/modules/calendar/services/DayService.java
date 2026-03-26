package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.calendar.dtos.days.CreateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.UpdateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.ViewDayDTO;
import com.mcommings.campaigner.modules.calendar.entities.Day;
import com.mcommings.campaigner.modules.calendar.mappers.DayMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IDayRepository;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DayService extends BaseService<
        Day,
        Integer,
        ViewDayDTO,
        CreateDayDTO,
        UpdateDayDTO> {

    private final IDayRepository dayRepository;
    private final ICampaignRepository campaignRepository;
    private final DayMapper dayMapper;

    @Override
    protected JpaRepository<Day, Integer> getRepository() {
        return dayRepository;
    }

    @Override
    protected ViewDayDTO toViewDto(Day entity) {
        return dayMapper.toDto(entity);
    }

    @Override
    protected Day toEntity(CreateDayDTO dto) {
        Day entity = dayMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateDayDTO dto, Day entity) {
        dayMapper.updateDayFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateDayDTO dto) {
        return dto.getId();
    }

    public List<ViewDayDTO> getDaysByCampaignUUID(UUID uuid) {
        return query(dayRepository::findByCampaign_Uuid, uuid);
    }

    public List<ViewDayDTO> getDaysByWeekId(int weekId) {
        return query(dayRepository::findByWeek_Id, weekId);
    }

}
