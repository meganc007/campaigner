package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.calendar.dtos.DayDTO;
import com.mcommings.campaigner.modules.calendar.mappers.DayMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IDayRepository;
import com.mcommings.campaigner.modules.calendar.services.interfaces.IDay;
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
public class DayService implements IDay {

    private final IDayRepository dayRepository;
    private final DayMapper dayMapper;

    @Override
    public List<DayDTO> getDays() {
        return dayRepository.findAll()
                .stream()
                .map(dayMapper::mapToDayDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DayDTO> getDay(int dayId) {
        return dayRepository.findById(dayId)
                .map(dayMapper::mapToDayDto);
    }

    @Override
    public List<DayDTO> getDaysByCampaignUUID(UUID uuid) {
        return dayRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(dayMapper::mapToDayDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DayDTO> getDaysByWeek(int weekId) {
        return dayRepository.findByfk_week(weekId)
                .stream()
                .map(dayMapper::mapToDayDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveDay(DayDTO day) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(day)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(dayRepository, day.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        dayMapper.mapToDayDto(
                dayRepository.save(dayMapper.mapFromDayDto(day)
                ));
    }

    @Override
    @Transactional
    public void deleteDay(int dayId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(dayRepository, dayId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        dayRepository.deleteById(dayId);
    }

    @Override
    @Transactional
    public Optional<DayDTO> updateDay(int dayId, DayDTO day) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(dayRepository, dayId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(day)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(dayRepository, day.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return dayRepository.findById(dayId).map(foundDay -> {
            if (day.getName() != null) foundDay.setName(day.getName());
            if (day.getDescription() != null) foundDay.setDescription(day.getDescription());
            if (day.getFk_campaign_uuid() != null) foundDay.setFk_campaign_uuid(day.getFk_campaign_uuid());
            if (day.getFk_week() != null) foundDay.setFk_week(day.getFk_week());

            return dayMapper.mapToDayDto(dayRepository.save(foundDay));
        });
    }

}
