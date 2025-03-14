package com.mcommings.campaigner.locations.services.interfaces;

import com.mcommings.campaigner.locations.dtos.PlaceTypeDTO;

import java.util.List;
import java.util.Optional;

public interface IPlaceType {

    List<PlaceTypeDTO> getPlaceTypes();

    Optional<PlaceTypeDTO> getPlaceType(int placeTypeId);

    void savePlaceType(PlaceTypeDTO placeType);

    void deletePlaceType(int placeTypeId);

    Optional<PlaceTypeDTO> updatePlaceType(int placeTypeId, PlaceTypeDTO placeType);
}
