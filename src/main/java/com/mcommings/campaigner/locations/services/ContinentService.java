package com.mcommings.campaigner.locations.services;

import com.mcommings.campaigner.common.entities.RepositoryHelper;
import com.mcommings.campaigner.locations.dtos.ContinentDTO;
import com.mcommings.campaigner.locations.mappers.ContinentMapper;
import com.mcommings.campaigner.locations.repositories.IContinentRepository;
import com.mcommings.campaigner.locations.services.interfaces.IContinent;
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
public class ContinentService implements IContinent {

    private final IContinentRepository continentRepository;
    private final ContinentMapper continentMapper;

    @Override
    public List<ContinentDTO> getContinents() {
        return continentRepository.findAll()
                .stream()
                .map(continentMapper::mapToContinentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ContinentDTO> getContinent(int continentId) {

        return continentRepository.findById(continentId)
                .map(continentMapper::mapToContinentDto);
    }

    @Override
    public List<ContinentDTO> getContinentsByCampaignUUID(UUID uuid) {

        return continentRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(continentMapper::mapToContinentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveContinent(ContinentDTO continent) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(continent)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(continentRepository, continent.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        continentMapper.mapToContinentDto(
                continentRepository.save(
                        continentMapper.mapFromContinentDto(continent)
                ));
    }

    @Override
    public void deleteContinent(int continentId) throws IllegalArgumentException {
        if (RepositoryHelper.cannotFindId(continentRepository, continentId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        continentRepository.deleteById(continentId);
    }

    @Override
    public Optional<ContinentDTO> updateContinent(int continentId, ContinentDTO continent) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(continentRepository, continentId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(continent)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(continentRepository, continent.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return continentRepository.findById(continentId).map(foundContinent -> {
            if (continent.getName() != null) foundContinent.setName(continent.getName());
            if (continent.getDescription() != null) foundContinent.setDescription(continent.getDescription());
            if (continent.getFk_campaign_uuid() != null)
                foundContinent.setFk_campaign_uuid(continent.getFk_campaign_uuid());

            return continentMapper.mapToContinentDto(continentRepository.save(foundContinent));
        });
    }
}
