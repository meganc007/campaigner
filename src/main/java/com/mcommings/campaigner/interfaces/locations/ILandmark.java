package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Landmark;

import java.util.List;

public interface ILandmark {

    List<Landmark> getLandmarks();

    Landmark getLandmark(int landmarkId);

    void saveLandmark(Landmark landmark);

    void deleteLandmark(int landmarkId);

    void updateLandmark(int landmarkId, Landmark landmark);
}
