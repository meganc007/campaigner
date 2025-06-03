package com.mcommings.campaigner.modules.people.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.people.dtos.RaceDTO;
import com.mcommings.campaigner.modules.people.mappers.RaceMapper;
import com.mcommings.campaigner.modules.people.repositories.IRaceRepository;
import com.mcommings.campaigner.modules.people.services.interfaces.IRace;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class RaceService implements IRace {

    private final IRaceRepository raceRepository;
    private final RaceMapper raceMapper;

    @Override
    public List<RaceDTO> getRaces() {

        return raceRepository.findAll()
                .stream()
                .map(raceMapper::mapToRaceDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RaceDTO> getRace(int raceId) {
        return raceRepository.findById(raceId)
                .map(raceMapper::mapToRaceDto);
    }

    @Override
    public List<RaceDTO> getRacesByIsExotic(boolean isExotic) {
        return raceRepository.findByIsExotic(isExotic)
                .stream()
                .map(raceMapper::mapToRaceDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveRace(RaceDTO race) throws IllegalArgumentException, DataIntegrityViolationException {

        if (RepositoryHelper.nameIsNullOrEmpty(race)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(raceRepository, race.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        raceMapper.mapToRaceDto(
                raceRepository.save(raceMapper.mapFromRaceDto(race))
        );
    }

    @Override
    public void deleteRace(int raceId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(raceRepository, raceId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        raceRepository.deleteById(raceId);
    }

    @Override
    public Optional<RaceDTO> updateRace(int raceId, RaceDTO race) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(raceRepository, raceId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(race)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExistsInAnotherRecord(raceRepository, race.getName(), raceId)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return raceRepository.findById(raceId).map(foundRace -> {
            if (race.getName() != null) foundRace.setName(race.getName());
            if (race.getDescription() != null) foundRace.setDescription(race.getDescription());
            if (race.getIsExotic() != null) foundRace.setIsExotic(race.getIsExotic());

            return raceMapper.mapToRaceDto(raceRepository.save(foundRace));
        });
    }
}
