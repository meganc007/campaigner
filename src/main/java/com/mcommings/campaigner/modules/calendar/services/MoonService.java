package com.mcommings.campaigner.modules.calendar.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.calendar.dtos.MoonDTO;
import com.mcommings.campaigner.modules.calendar.mappers.MoonMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IMoonRepository;
import com.mcommings.campaigner.modules.calendar.services.interfaces.IMoon;
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
public class MoonService implements IMoon {

    private final IMoonRepository moonRepository;
    private final MoonMapper moonMapper;

    @Override
    public List<MoonDTO> getMoons() {

        return moonRepository.findAll()
                .stream()
                .map(moonMapper::mapToMoonDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MoonDTO> getMoon(int moonId) {
        return moonRepository.findById(moonId)
                .map(moonMapper::mapToMoonDto);
    }

    @Override
    public List<MoonDTO> getMoonsByCampaignUUID(UUID uuid) {

        return moonRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(moonMapper::mapToMoonDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveMoon(MoonDTO moon) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(moon)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(moonRepository, moon.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        moonMapper.mapToMoonDto(
                moonRepository.save(moonMapper.mapFromMoonDto(moon))
        );
    }

    @Override
    @Transactional
    public void deleteMoon(int moonId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(moonRepository, moonId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        moonRepository.deleteById(moonId);
    }

    @Override
    @Transactional
    public Optional<MoonDTO> updateMoon(int moonId, MoonDTO moon) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(moonRepository, moonId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(moon)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExistsInAnotherRecord(moonRepository, moon.getName(), moonId)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return moonRepository.findById(moonId).map(foundMoon -> {
            if (moon.getName() != null) foundMoon.setName(moon.getName());
            if (moon.getDescription() != null) foundMoon.setDescription(moon.getDescription());
            if (moon.getFk_campaign_uuid() != null) foundMoon.setFk_campaign_uuid(moon.getFk_campaign_uuid());

            return moonMapper.mapToMoonDto(moonRepository.save(foundMoon));
        });
    }

}
