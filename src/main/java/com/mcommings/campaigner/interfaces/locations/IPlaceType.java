package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.entities.locations.PlaceType;

import java.util.List;

public interface IPlaceType {

    List<PlaceType> getPlaceTypes();

    void savePlaceType(PlaceType placeType);

    void deletePlaceType(int placeTypeId);

    void updatePlaceType(int placeTypeId, PlaceType placeType);
}
