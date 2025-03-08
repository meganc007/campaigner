package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.entities.locations.Landmark;

import java.util.List;
import java.util.UUID;

public interface ILandmark {

    List<Landmark> getLandmarks();

    Landmark getLandmark(int landmarkId);

    List<Landmark> getLandmarksByCampaignUUID(UUID uuid);

    List<Landmark> getLandmarksByRegionId(int regionId);

    void saveLandmark(Landmark landmark);

    void deleteLandmark(int landmarkId);

    void updateLandmark(int landmarkId, Landmark landmark);
}
