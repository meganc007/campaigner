package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Place;

import java.util.List;

public interface IPlace {

    List<Place> getPlaces();

    void savePlace(Place place);

    void deletePlace(int placeId);

    void updatePlace(int placeId, Place place);
}
