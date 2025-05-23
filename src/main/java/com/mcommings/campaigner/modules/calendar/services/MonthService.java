package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.calendar.dtos.MonthDTO;
import com.mcommings.campaigner.modules.calendar.mappers.MonthMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IMonthRepository;
import com.mcommings.campaigner.modules.calendar.services.interfaces.IMonth;
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
public class MonthService implements IMonth {

    private final IMonthRepository monthRepository;
    private final MonthMapper monthMapper;

    @Override
    public List<MonthDTO> getMonths() {
        return monthRepository.findAll()
                .stream()
                .map(monthMapper::mapToMonthDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MonthDTO> getMonth(int monthId) {
        return monthRepository.findById(monthId)
                .map(monthMapper::mapToMonthDto);
    }

    @Override
    public List<MonthDTO> getMonthsByCampaignUUID(UUID uuid) {
        return monthRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(monthMapper::mapToMonthDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveMonth(MonthDTO month) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(month)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(monthRepository, month.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        monthMapper.mapToMonthDto(
                monthRepository.save(monthMapper.mapFromMonthDto(month))
        );
    }

    @Override
    @Transactional
    public void deleteMonth(int monthId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(monthRepository, monthId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        monthRepository.deleteById(monthId);
    }

    @Override
    @Transactional
    public Optional<MonthDTO> updateMonth(int monthId, MonthDTO month) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(monthRepository, monthId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(month)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(monthRepository, month.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return monthRepository.findById(monthId).map(foundMonth -> {
            if (month.getName() != null) foundMonth.setName(month.getName());
            if (month.getDescription() != null) foundMonth.setDescription(month.getDescription());
            if (month.getFk_campaign_uuid() != null) foundMonth.setFk_campaign_uuid(month.getFk_campaign_uuid());
            if (month.getSeason() != null) foundMonth.setSeason(month.getSeason());

            return monthMapper.mapToMonthDto(monthRepository.save(foundMonth));
        });
    }

}
