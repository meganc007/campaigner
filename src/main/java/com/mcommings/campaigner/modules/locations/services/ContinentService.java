package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.continents.CreateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.UpdateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.ViewContinentDTO;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.modules.locations.mappers.ContinentMapper;
import com.mcommings.campaigner.modules.locations.repositories.IContinentRepository;
import com.mcommings.campaigner.modules.locations.services.interfaces.IContinent;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.NAME_EXISTS;
import static com.mcommings.campaigner.enums.ErrorMessage.NULL_OR_EMPTY;

@Service
@RequiredArgsConstructor
public class ContinentService implements IContinent {

    private final IContinentRepository continentRepository;
    private final ICampaignRepository campaignRepository;
    private final ContinentMapper continentMapper;

    @Override
    public List<ViewContinentDTO> getContinents() {
        return continentRepository.findAll()
                .stream()
                .map(continentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ViewContinentDTO> getContinent(int continentId) {

        return continentRepository.findById(continentId)
                .map(continentMapper::toDto);
    }

    @Override
    public List<ViewContinentDTO> getContinentsByCampaignUUID(UUID uuid) {

        return continentRepository.findByCampaign_Uuid(uuid)
                .stream()
                .map(continentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ViewContinentDTO saveContinent(CreateContinentDTO continent) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(continent)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(continentRepository, continent.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        Continent entity = continentMapper.toEntity(continent);

        Campaign campaign =
                campaignRepository.getReferenceById(continent.getCampaignUuid());

        entity.setCampaign(campaign);

        Continent saved = continentRepository.save(entity);

        return continentMapper.toDto(saved);
    }

    @Override
    public Optional<ViewContinentDTO> updateContinent(UpdateContinentDTO continent) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(continent)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (continentRepository.existsByNameAndCampaign_UuidAndIdNot(
                continent.getName(),
                continent.getCampaignUuid(),
                continent.getId())) {

            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return continentRepository.findById(continent.getId()).map(foundContinent -> {

            if (continent.getName() != null)
                foundContinent.setName(continent.getName());

            if (continent.getDescription() != null)
                foundContinent.setDescription(continent.getDescription());

            if (continent.getCampaignUuid() != null)
                foundContinent.setCampaign(
                        campaignRepository.getReferenceById(continent.getCampaignUuid())
                );

            return continentMapper.toDto(
                    continentRepository.save(foundContinent)
            );
        });
    }

    @Override
    public void deleteContinent(int continentId) throws IllegalArgumentException {
        continentRepository.deleteById(continentId);
    }
}
