package com.mcommings.campaigner.locations.services.interfaces;

import com.mcommings.campaigner.locations.dtos.PlaceDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPlace {

    List<PlaceDTO> getPlaces();

    Optional<PlaceDTO> getPlace(int placeId);

    List<PlaceDTO> getPlacesByCampaignUUID(UUID uuid);

    List<PlaceDTO> getPlacesByCountryId(int countryId);

    List<PlaceDTO> getPlacesByCityId(int cityId);

    List<PlaceDTO> getPlacesByRegionId(int regionId);

    void savePlace(PlaceDTO place);

    void deletePlace(int placeId);

    Optional<PlaceDTO> updatePlace(int placeId, PlaceDTO place);
}
