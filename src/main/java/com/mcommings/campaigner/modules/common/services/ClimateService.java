package com.mcommings.campaigner.modules.common.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.common.dtos.ClimateDTO;
import com.mcommings.campaigner.modules.common.mappers.ClimateMapper;
import com.mcommings.campaigner.modules.common.repositories.IClimateRepository;
import com.mcommings.campaigner.modules.common.services.interfaces.IClimate;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class ClimateService implements IClimate {

    private final IClimateRepository climateRepository;
    private final ClimateMapper climateMapper;

    @Override
    public List<ClimateDTO> getClimates() {

        return climateRepository.findAll()
                .stream()
                .map(climateMapper::mapToClimateDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ClimateDTO> getClimate(int climateId) {
        return climateRepository.findById(climateId)
                .map(climateMapper::mapToClimateDto);
    }

    @Override
    public void saveClimate(ClimateDTO climate) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(climate)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(climateRepository, climate.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        climateMapper.mapToClimateDto(
                climateRepository.save(
                        climateMapper.mapFromClimateDto(climate))
        );
    }

    @Override
    public void deleteClimate(int climateId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(climateRepository, climateId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        climateRepository.deleteById(climateId);
    }

    @Override
    public Optional<ClimateDTO> updateClimate(int climateId, ClimateDTO climate) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(climateRepository, climateId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(climate)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExistsInAnotherRecord(climateRepository, climate.getName(), climateId)) {
            throw new IllegalArgumentException(NAME_EXISTS.message);
        }

        return climateRepository.findById(climateId).map(foundClimate -> {
            if (climate.getName() != null) foundClimate.setName(climate.getName());
            if (climate.getDescription() != null) foundClimate.setDescription(climate.getDescription());

            return climateMapper.mapToClimateDto(climateRepository.save(foundClimate));
        });
    }

}
