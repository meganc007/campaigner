package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Landmark;

import java.util.List;
import java.util.UUID;

public interface ILandmark {

    List<Landmark> getLandmarks();

    Landmark getLandmark(int landmarkId);

    List<Landmark> getLandmarksByCampaignUUID(UUID uuid);

    void saveLandmark(Landmark landmark);

    void deleteLandmark(int landmarkId);

    void updateLandmark(int landmarkId, Landmark landmark);
}
