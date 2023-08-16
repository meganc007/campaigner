package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.PlaceType;

import java.util.List;

public interface IPlaceType {

    List<PlaceType> getPlaceTypes();

    void savePlaceType(PlaceType placeType);

    void deletePlaceType(int placeTypeId);

    void updatePlaceType(int placeTypeId, PlaceType placeType);
}
