package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.CreateLandmarkDTO;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.UpdateLandmarkDTO;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.ViewLandmarkDTO;
import com.mcommings.campaigner.modules.locations.entities.Landmark;
import com.mcommings.campaigner.modules.locations.mappers.LandmarkMapper;
import com.mcommings.campaigner.modules.locations.repositories.ILandmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LandmarkService extends BaseService<
        Landmark,
        Integer,
        ViewLandmarkDTO,
        CreateLandmarkDTO,
        UpdateLandmarkDTO> {

    private final ILandmarkRepository landmarkRepository;
    private final ICampaignRepository campaignRepository;
    private final LandmarkMapper landmarkMapper;

    @Override
    protected JpaRepository<Landmark, Integer> getRepository() {
        return landmarkRepository;
    }

    @Override
    protected ViewLandmarkDTO toViewDto(Landmark entity) {
        return landmarkMapper.toDto(entity);
    }

    @Override
    protected Landmark toEntity(CreateLandmarkDTO dto) {
        Landmark entity = landmarkMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateLandmarkDTO dto, Landmark entity) {
        landmarkMapper.updateLandmarkFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateLandmarkDTO dto) {
        return dto.getId();
    }


    public List<ViewLandmarkDTO> getLandmarksByCampaignUUID(UUID uuid) {
        return query(landmarkRepository::findByCampaign_Uuid, uuid);
    }


    public List<ViewLandmarkDTO> getLandmarksByRegionId(int regionId) {
        return query(landmarkRepository::findByRegion_Id, regionId);
    }

}
