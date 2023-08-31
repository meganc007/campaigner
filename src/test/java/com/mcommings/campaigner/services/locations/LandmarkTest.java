package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.models.locations.Landmark;
import com.mcommings.campaigner.repositories.locations.ILandmarkRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LandmarkTest {
    @Mock
    private ILandmarkRepository landmarkRepository;

    @InjectMocks
    private LandmarkService landmarkService;

    @Test
    public void whenThereAreLandmarks_getLandmarks_ReturnsLandmarks() {
        List<Landmark> landmarks = new ArrayList<>();
        landmarks.add(new Landmark(1, "Landmark 1", "Description 1"));
        landmarks.add(new Landmark(2, "Landmark 2", "Description 2"));
        when(landmarkRepository.findAll()).thenReturn(landmarks);

        List<Landmark> result = landmarkService.getLandmarks();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(landmarks, result);
    }

    @Test
    public void whenThereAreNoLandmarks_getLandmarks_ReturnsNothing() {
        List<Landmark> landmarks = new ArrayList<>();
        when(landmarkRepository.findAll()).thenReturn(landmarks);

        List<Landmark> result = landmarkService.getLandmarks();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(landmarks, result);
    }

    @Test
    public void whenLandmarkIsValid_saveLandmark_SavesTheLandmark() {
        Landmark landmark = new Landmark(1, "Landmark 1", "Description 1");
        when(landmarkRepository.saveAndFlush(landmark)).thenReturn(landmark);

        assertDoesNotThrow(() -> landmarkService.saveLandmark(landmark));
        verify(landmarkRepository, times(1)).saveAndFlush(landmark);
    }

    @Test
    public void whenLandmarkNameIsInvalid_saveLandmark_ThrowsIllegalArgumentException() {
        Landmark landmarkWithEmptyName = new Landmark(1, "", "Description 1");
        Landmark landmarkWithNullName = new Landmark(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> landmarkService.saveLandmark(landmarkWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> landmarkService.saveLandmark(landmarkWithNullName));
    }

    @Test
    public void whenLandmarkNameAlreadyExists_saveLandmark_ThrowsDataIntegrityViolationException() {
        Landmark landmark = new Landmark(1, "Landmark 1", "Description 1");
        Landmark landmarkWithDuplicatedName = new Landmark(2, "Landmark 1", "Description 2");
        when(landmarkRepository.saveAndFlush(landmark)).thenReturn(landmark);
        when(landmarkRepository.saveAndFlush(landmarkWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> landmarkService.saveLandmark(landmark));
        assertThrows(DataIntegrityViolationException.class, () -> landmarkService.saveLandmark(landmarkWithDuplicatedName));
    }

    @Test
    public void whenLandmarkIdExists_deleteLandmark_DeletesTheLandmark() {
        int landmarkId = 1;
        when(landmarkRepository.existsById(landmarkId)).thenReturn(true);
        assertDoesNotThrow(() -> landmarkService.deleteLandmark(landmarkId));
        verify(landmarkRepository, times(1)).deleteById(landmarkId);
    }

    @Test
    public void whenLandmarkIdDoesNotExist_deleteLandmark_ThrowsIllegalArgumentException() {
        int landmarkId = 9000;
        when(landmarkRepository.existsById(landmarkId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> landmarkService.deleteLandmark(landmarkId));
    }

    //TODO: test that deleteLandmark doesn't delete when it's a foreign key

    @Test
    public void whenLandmarkIdIsFound_updateLandmark_UpdatesTheLandmark() {
        int landmarkId = 1;
        Landmark landmark = new Landmark(landmarkId, "Old Landmark Name", "Old Description");
        Landmark landmarkToUpdate = new Landmark(landmarkId, "Updated Landmark Name", "Updated Description");

        when(landmarkRepository.existsById(landmarkId)).thenReturn(true);
        when(landmarkRepository.findById(landmarkId)).thenReturn(Optional.of(landmark));

        landmarkService.updateLandmark(landmarkId, landmarkToUpdate);

        verify(landmarkRepository).findById(landmarkId);

        Landmark result = landmarkRepository.findById(landmarkId).get();
        Assertions.assertEquals(landmarkToUpdate.getName(), result.getName());
        Assertions.assertEquals(landmarkToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenLandmarkIdIsNotFound_updateLandmark_ThrowsIllegalArgumentException() {
        int landmarkId = 1;
        Landmark landmark = new Landmark(landmarkId, "Old Landmark Name", "Old Description");

        when(landmarkRepository.existsById(landmarkId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> landmarkService.updateLandmark(landmarkId, landmark));
    }
}
