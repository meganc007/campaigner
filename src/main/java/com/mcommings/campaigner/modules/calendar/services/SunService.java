package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.calendar.dtos.SunDTO;
import com.mcommings.campaigner.modules.calendar.mappers.SunMapper;
import com.mcommings.campaigner.modules.calendar.repositories.ISunRepository;
import com.mcommings.campaigner.modules.calendar.services.interfaces.ISun;
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
public class SunService implements ISun {

    private final ISunRepository sunRepository;
    private final SunMapper sunMapper;

    @Override
    public List<SunDTO> getSuns() {

        return sunRepository.findAll()
                .stream()
                .map(sunMapper::mapToSunDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SunDTO> getSun(int sunId) {
        return sunRepository.findById(sunId)
                .map(sunMapper::mapToSunDto);
    }

    @Override
    public List<SunDTO> getSunsByCampaignUUID(UUID uuid) {

        return sunRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(sunMapper::mapToSunDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveSun(SunDTO sun) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(sun)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(sunRepository, sun.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        sunMapper.mapToSunDto(
                sunRepository.save(sunMapper.mapFromSunDto(sun)
                ));
    }

    @Override
    @Transactional
    public void deleteSun(int sunId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(sunRepository, sunId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        sunRepository.deleteById(sunId);
    }

    @Override
    @Transactional
    public Optional<SunDTO> updateSun(int sunId, SunDTO sun) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(sunRepository, sunId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(sun)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(sunRepository, sun.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return sunRepository.findById(sunId).map(foundSun -> {
            if (sun.getName() != null) foundSun.setName(sun.getName());
            if (sun.getDescription() != null) foundSun.setDescription(sun.getDescription());
            if (sun.getFk_campaign_uuid() != null) foundSun.setFk_campaign_uuid(sun.getFk_campaign_uuid());

            return sunMapper.mapToSunDto(sunRepository.save(foundSun));
        });
    }

}
