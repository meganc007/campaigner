package com.mcommings.campaigner.locations.services.interfaces;

import com.mcommings.campaigner.locations.dtos.LandmarkDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ILandmark {

    List<LandmarkDTO> getLandmarks();

    Optional<LandmarkDTO> getLandmark(int landmarkId);

    List<LandmarkDTO> getLandmarksByCampaignUUID(UUID uuid);

    List<LandmarkDTO> getLandmarksByRegionId(int regionId);

    void saveLandmark(LandmarkDTO landmark);

    void deleteLandmark(int landmarkId);

    Optional<LandmarkDTO> updateLandmark(int landmarkId, LandmarkDTO landmark);
}
