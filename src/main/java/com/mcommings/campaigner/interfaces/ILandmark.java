package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Landmark;

import java.util.List;

public interface ILandmark {
    
    List<Landmark> getLandmarks();

    void saveLandmark(Landmark landmark);

    void deleteLandmark(int landmarkId);

    void updateLandmark(int landmarkId, Landmark landmark);
}
