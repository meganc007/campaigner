package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.calendar.dtos.WeekDTO;
import com.mcommings.campaigner.modules.calendar.mappers.WeekMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IWeekRepository;
import com.mcommings.campaigner.modules.calendar.services.interfaces.IWeek;
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
public class WeekService implements IWeek {

    private final IWeekRepository weekRepository;
    private final WeekMapper weekMapper;

    @Override
    public List<WeekDTO> getWeeks() {
        return weekRepository.findAll()
                .stream()
                .map(weekMapper::mapToWeekDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WeekDTO> getWeek(int weekId) {
        return weekRepository.findById(weekId)
                .map(weekMapper::mapToWeekDto);
    }

    @Override
    public List<WeekDTO> getWeeksByCampaignUUID(UUID uuid) {
        return weekRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(weekMapper::mapToWeekDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<WeekDTO> getWeeksByMonth(int monthId) {

        return weekRepository.findByfk_month(monthId)
                .stream()
                .map(weekMapper::mapToWeekDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveWeek(WeekDTO week) throws DataIntegrityViolationException {
        // TODO: Change this to check if UUID is null or empty
//        if (RepositoryHelper.nameIsNullOrEmpty(week)) {
//            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
//        }

        weekMapper.mapToWeekDto(
                weekRepository.save(weekMapper.mapFromWeekDto(week)
                ));
    }

    @Override
    @Transactional
    public void deleteWeek(int weekId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weekRepository, weekId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        weekRepository.deleteById(weekId);
    }

    @Override
    @Transactional
    public Optional<WeekDTO> updateWeek(int weekId, WeekDTO week) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weekRepository, weekId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(week)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }

        return weekRepository.findById(weekId).map(foundWeek -> {
            if (week.getDescription() != null) foundWeek.setDescription(week.getDescription());
            if (week.getFk_campaign_uuid() != null) foundWeek.setFk_campaign_uuid(week.getFk_campaign_uuid());
            if (week.getWeek_number() != null) foundWeek.setWeek_number(week.getWeek_number());
            if (week.getFk_month() != null) foundWeek.setFk_month(week.getFk_month());

            return weekMapper.mapToWeekDto(weekRepository.save(foundWeek));
        });
    }
}
