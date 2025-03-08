package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.dtos.ContinentDTO;
import com.mcommings.campaigner.interfaces.locations.IContinent;
import com.mcommings.campaigner.mappers.ContinentMapper;
import com.mcommings.campaigner.repositories.locations.IContinentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class ContinentService implements IContinent {
    private final IContinentRepository continentRepository;
    private final ContinentMapper continentMapper;

    @Override
    public List<ContinentDTO> getContinents() {
        return continentRepository.findAll()
                .stream()
                .map(continentMapper::continentToContinentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ContinentDTO> getContinent(int continentId) {

        return continentRepository.findById(continentId)
                .map(continentMapper::continentToContinentDto);
    }

    @Override
    public List<ContinentDTO> getContinentsByCampaignUUID(UUID uuid) {

        return continentRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(continentMapper::continentToContinentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveContinent(ContinentDTO continent) {
        continentMapper.continentToContinentDto(
                continentRepository.save(
                        continentMapper.continentDtotoContinent(continent)
                ));
    }

    @Override
    public Boolean deleteContinent(int continentId) {
        if (continentRepository.existsById(continentId)) {
            continentRepository.deleteById(continentId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<ContinentDTO> updateContinent(int continentId, ContinentDTO continent) {
        return continentRepository.findById(continentId).map(foundContinent -> {
            if (continent.getName() != null) foundContinent.setName(continent.getName());
            if (continent.getDescription() != null) foundContinent.setDescription(continent.getDescription());
            if (continent.getFk_campaign_uuid() != null)
                foundContinent.setFk_campaign_uuid(continent.getFk_campaign_uuid());

            return continentMapper.continentToContinentDto(continentRepository.save(foundContinent));
        });
    }
}
