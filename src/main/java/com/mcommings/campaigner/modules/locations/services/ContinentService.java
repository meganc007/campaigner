package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.continents.CreateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.UpdateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.ViewContinentDTO;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.modules.locations.mappers.ContinentMapper;
import com.mcommings.campaigner.modules.locations.repositories.IContinentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContinentService extends BaseService<
        Continent,
        Integer,
        ViewContinentDTO,
        CreateContinentDTO,
        UpdateContinentDTO> {

    private final IContinentRepository continentRepository;
    private final ICampaignRepository campaignRepository;
    private final ContinentMapper continentMapper;

    @Override
    protected JpaRepository<Continent, Integer> getRepository() {
        return continentRepository;
    }

    @Override
    protected ViewContinentDTO toViewDto(Continent entity) {
        return continentMapper.toDto(entity);
    }

    @Override
    protected Continent toEntity(CreateContinentDTO dto) {

        Continent entity = continentMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );

        return entity;
    }

    @Override
    protected void updateEntity(UpdateContinentDTO dto, Continent entity) {

        continentMapper.updateContinentFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateContinentDTO dto) {
        return dto.getId();
    }

    public List<ViewContinentDTO> getContinentsByCampaignUUID(UUID uuid) {

        return query(continentRepository::findByCampaign_Uuid, uuid);
    }
}
