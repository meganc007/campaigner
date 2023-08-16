package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.PlaceType;

import java.util.List;

public interface IPlaceType {

    public List<PlaceType> getPlaceTypes();

    public void savePlaceType(PlaceType placeType);

    public void deletePlaceType(int placeTypeId);

    public void updatePlaceType(int placeTypeId, PlaceType placeType);
}
