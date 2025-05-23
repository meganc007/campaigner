package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.locations.dtos.PlaceDTO;
import com.mcommings.campaigner.modules.locations.mappers.PlaceMapper;
import com.mcommings.campaigner.modules.locations.repositories.IPlaceRepository;
import com.mcommings.campaigner.modules.locations.services.interfaces.IPlace;
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
public class PlaceService implements IPlace {

    private final IPlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    @Override
    public List<PlaceDTO> getPlaces() {
        return placeRepository.findAll()
                .stream()
                .map(placeMapper::mapToPlaceDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PlaceDTO> getPlace(int placeId) {
        return placeRepository.findById(placeId)
                .map(placeMapper::mapToPlaceDto);
    }

    @Override
    public List<PlaceDTO> getPlacesByCampaignUUID(UUID uuid) {
        return placeRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(placeMapper::mapToPlaceDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaceDTO> getPlacesByCountryId(int countryId) {
        return placeRepository.findByfk_country(countryId)
                .stream()
                .map(placeMapper::mapToPlaceDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaceDTO> getPlacesByCityId(int cityId) {
        return placeRepository.findByfk_city(cityId)
                .stream()
                .map(placeMapper::mapToPlaceDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaceDTO> getPlacesByRegionId(int regionId) {
        return placeRepository.findByfk_region(regionId)
                .stream()
                .map(placeMapper::mapToPlaceDto)
                .collect(Collectors.toList());
    }

    @Override
    public void savePlace(PlaceDTO place) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(place)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(placeRepository, place.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        placeMapper.mapToPlaceDto(
                placeRepository.save(placeMapper.mapFromPlaceDto(place))
        );
    }

    @Override
    public void deletePlace(int placeId) throws IllegalArgumentException {
        if (RepositoryHelper.cannotFindId(placeRepository, placeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        placeRepository.deleteById(placeId);
    }

    @Override
    public Optional<PlaceDTO> updatePlace(int placeId, PlaceDTO place) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(placeRepository, placeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(place)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(placeRepository, place.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return placeRepository.findById(placeId).map(foundPlace -> {
            if (place.getName() != null) foundPlace.setName(place.getName());
            if (place.getDescription() != null) foundPlace.setDescription(place.getDescription());
            if (place.getFk_campaign_uuid() != null) foundPlace.setFk_campaign_uuid(place.getFk_campaign_uuid());
            if (place.getFk_place_type() != null) foundPlace.setFk_place_type(place.getFk_place_type());
            if (place.getFk_terrain() != null) foundPlace.setFk_terrain(place.getFk_terrain());
            if (place.getFk_country() != null) foundPlace.setFk_country(place.getFk_country());
            if (place.getFk_city() != null) foundPlace.setFk_city(place.getFk_city());
            if (place.getFk_region() != null) foundPlace.setFk_region(place.getFk_region());

            return placeMapper.mapToPlaceDto(placeRepository.save(foundPlace));
        });
    }
}
