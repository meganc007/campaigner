package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.places.CreatePlaceDTO;
import com.mcommings.campaigner.modules.locations.dtos.places.UpdatePlaceDTO;
import com.mcommings.campaigner.modules.locations.dtos.places.ViewPlaceDTO;
import com.mcommings.campaigner.modules.locations.entities.Place;
import com.mcommings.campaigner.modules.locations.mappers.PlaceMapper;
import com.mcommings.campaigner.modules.locations.repositories.IPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceService extends BaseService<
        Place,
        Integer,
        ViewPlaceDTO,
        CreatePlaceDTO,
        UpdatePlaceDTO> {

    private final IPlaceRepository placeRepository;
    private final ICampaignRepository campaignRepository;
    private final PlaceMapper placeMapper;

    @Override
    protected JpaRepository<Place, Integer> getRepository() {
        return placeRepository;
    }

    @Override
    protected ViewPlaceDTO toViewDto(Place entity) {
        return placeMapper.toDto(entity);
    }

    @Override
    protected Place toEntity(CreatePlaceDTO dto) {
        Place entity = placeMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdatePlaceDTO dto, Place entity) {
        placeMapper.updatePlaceFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdatePlaceDTO dto) {
        return dto.getId();
    }

    public List<ViewPlaceDTO> getPlacesByCampaignUUID(UUID uuid) {
        return query(placeRepository::findByCampaign_Uuid, uuid);
    }

    public List<ViewPlaceDTO> getPlacesByPlaceTypeId(int placeTypeId) {
        return query(placeRepository::findByPlaceType_Id, placeTypeId);
    }

    public List<ViewPlaceDTO> getPlacesByTerrainId(int terrainId) {
        return query(placeRepository::findByTerrain_Id, terrainId);
    }

    public List<ViewPlaceDTO> getPlacesByCountryId(int countryId) {
        return query(placeRepository::findByCountry_Id, countryId);
    }

    public List<ViewPlaceDTO> getPlacesByCityId(int cityId) {
        return query(placeRepository::findByCity_Id, cityId);
    }

    public List<ViewPlaceDTO> getPlacesByRegionId(int regionId) {
        return query(placeRepository::findByRegion_Id, regionId);
    }

}
