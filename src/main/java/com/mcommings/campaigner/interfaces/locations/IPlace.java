package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Place;

import java.util.List;
import java.util.UUID;

public interface IPlace {

    List<Place> getPlaces();

    Place getPlace(int placeId);

    List<Place> getPlacesByCampaignUUID(UUID uuid);

    void savePlace(Place place);

    void deletePlace(int placeId);

    void updatePlace(int placeId, Place place);
}
