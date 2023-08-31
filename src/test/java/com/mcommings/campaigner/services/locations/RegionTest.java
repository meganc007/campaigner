package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.Landmark;
import com.mcommings.campaigner.models.locations.Place;
import com.mcommings.campaigner.models.locations.Region;
import com.mcommings.campaigner.repositories.IClimateRepository;
import com.mcommings.campaigner.repositories.locations.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mcommings.campaigner.enums.ForeignKey.FK_REGION;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegionTest {
    @Mock
    private IRegionRepository regionRepository;
    @Mock
    private ICountryRepository countryRepository;
    @Mock
    private IClimateRepository climateRepository;
    @Mock
    private ICityRepository cityRepository;
    @Mock
    private ILandmarkRepository landmarkRepository;
    @Mock
    private IPlaceRepository placeRepository;

    @InjectMocks
    private RegionService regionService;

    @Test
    public void whenThereAreRegions_getRegions_ReturnsRegions() {
        List<Region> regions = new ArrayList<>();
        regions.add(new Region(1, "Region 1", "Description 1"));
        regions.add(new Region(2, "Region 2", "Description 2"));
        regions.add(new Region(3, "Region 3", "Description 3", 1, 2));
        when(regionRepository.findAll()).thenReturn(regions);

        List<Region> result = regionService.getRegions();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(regions, result);
    }

    @Test
    public void whenThereAreNoRegions_getRegions_ReturnsNothing() {
        List<Region> regions = new ArrayList<>();
        when(regionRepository.findAll()).thenReturn(regions);

        List<Region> result = regionService.getRegions();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(regions, result);
    }

    @Test
    public void whenRegionWithNoForeignKeysIsValid_saveRegion_SavesTheRegion() {
        Region region = new Region(1, "Region 1", "Description 1");
        when(regionRepository.saveAndFlush(region)).thenReturn(region);

        assertDoesNotThrow(() -> regionService.saveRegion(region));

        verify(regionRepository, times(1)).saveAndFlush(region);
    }

    @Test
    public void whenRegionWithForeignKeysIsValid_saveRegion_SavesTheRegion() {
        Region region = new Region(1, "Region 1", "Description 1", 1, 2);

        when(countryRepository.existsById(1)).thenReturn(true);
        when(climateRepository.existsById(2)).thenReturn(true);
        when(regionRepository.saveAndFlush(region)).thenReturn(region);

        assertDoesNotThrow(() -> regionService.saveRegion(region));

        verify(regionRepository, times(1)).saveAndFlush(region);
    }

    @Test
    public void whenRegionNameIsInvalid_saveRegion_ThrowsIllegalArgumentException() {
        Region regionWithEmptyName = new Region(1, "", "Description 1");
        Region regionWithNullName = new Region(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> regionService.saveRegion(regionWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> regionService.saveRegion(regionWithNullName));
    }

    @Test
    public void whenRegionNameAlreadyExists_saveRegion_ThrowsDataIntegrityViolationException() {
        Region region = new Region(1, "Region 1", "Description 1", 1, 2);
        Region regionWithDuplicatedName = new Region(2, "Region 1", "Description 2", 3, 4);

        when(regionRepository.existsById(1)).thenReturn(true);
        when(countryRepository.existsById(1)).thenReturn(true);
        when(climateRepository.existsById(2)).thenReturn(true);

        when(regionRepository.existsById(2)).thenReturn(true);
        when(countryRepository.existsById(3)).thenReturn(true);
        when(climateRepository.existsById(4)).thenReturn(true);

        when(regionRepository.saveAndFlush(region)).thenReturn(region);
        when(regionRepository.saveAndFlush(regionWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> regionService.saveRegion(region));
        assertThrows(DataIntegrityViolationException.class, () -> regionService.saveRegion(regionWithDuplicatedName));
    }

    @Test
    public void whenRegionHasInvalidForeignKeys_saveRegion_ThrowsDataIntegrityViolationException() {
        Region region = new Region(1, "Region 1", "Description 1", 1, 2);

        when(countryRepository.existsById(1)).thenReturn(false);
        when(climateRepository.existsById(3)).thenReturn(false);
        when(regionRepository.saveAndFlush(region)).thenReturn(region);

        assertThrows(DataIntegrityViolationException.class, () -> regionService.saveRegion(region));

    }

    @Test
    public void whenRegionIdExists_deleteRegion_DeletesTheRegion() {
        int regionId = 1;
        when(regionRepository.existsById(regionId)).thenReturn(true);
        assertDoesNotThrow(() -> regionService.deleteRegion(regionId));
        verify(regionRepository, times(1)).deleteById(regionId);
    }

    @Test
    public void whenRegionIdDoesNotExist_deleteRegion_ThrowsIllegalArgumentException() {
        int regionId = 9000;
        when(regionRepository.existsById(regionId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> regionService.deleteRegion(regionId));
    }

    @Test
    public void whenRegionIdIsAForeignKey_deleteRegion_ThrowsDataIntegrityViolationException() {
        int regionId = 1;
        Place place = new Place(1, "Place", "Description", 1, 1, 1, 1, regionId);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(cityRepository, placeRepository, regionRepository));
        List<Place> places = new ArrayList<>(Arrays.asList(place));

        Landmark landmark = new Landmark(1, "Landmark", "Description", regionId);
        List<Landmark> landmarks = new ArrayList<>(Arrays.asList(landmark));

        when(regionRepository.existsById(regionId)).thenReturn(true);
        when(placeRepository.existsById(regionId)).thenReturn(true);
        when(landmarkRepository.existsById(regionId)).thenReturn(true);
        when(placeRepository.findByfk_region(regionId)).thenReturn(places);
        when(landmarkRepository.findByfk_region(regionId)).thenReturn(landmarks);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_REGION.columnName, regionId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> regionService.deleteRegion(regionId));
    }


    @Test
    public void whenRegionIdWithNoFKIsFound_updateRegion_UpdatesTheRegion() {
        int regionId1 = 1;

        Region region = new Region(regionId1, "Old Region Name", "Old Description");
        Region regionToUpdateNoFK = new Region(regionId1, "Updated Region Name", "Updated Description");

        when(regionRepository.existsById(regionId1)).thenReturn(true);
        when(regionRepository.findById(regionId1)).thenReturn(Optional.of(region));

        regionService.updateRegion(regionId1, regionToUpdateNoFK);

        verify(regionRepository).findById(regionId1);

        Region result1 = regionRepository.findById(regionId1).get();
        Assertions.assertEquals(regionToUpdateNoFK.getName(), result1.getName());
        Assertions.assertEquals(regionToUpdateNoFK.getDescription(), result1.getDescription());
    }

    @Test
    public void whenRegionIdWithValidFKIsFound_updateRegion_UpdatesTheRegion() {
        int regionId = 2;

        Region region = new Region(regionId, "Test Region Name", "Test Description");
        Region regionToUpdate = new Region(regionId, "Updated Region Name", "Updated Description", 1, 2);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(countryRepository, climateRepository));

        when(regionRepository.existsById(regionId)).thenReturn(true);
        when(regionRepository.findById(regionId)).thenReturn(Optional.of(region));
        when(countryRepository.existsById(1)).thenReturn(true);
        when(climateRepository.existsById(2)).thenReturn(true);

        regionService.updateRegion(regionId, regionToUpdate);

        verify(regionRepository).findById(regionId);

        Region result = regionRepository.findById(regionId).get();
        Assertions.assertEquals(regionToUpdate.getName(), result.getName());
        Assertions.assertEquals(regionToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(regionToUpdate.getFk_country(), result.getFk_country());
        Assertions.assertEquals(regionToUpdate.getFk_climate(), result.getFk_climate());
    }

    @Test
    public void whenRegionIdWithInvalidFKIsFound_updateRegion_ThrowsDataIntegrityViolationException() {
        int regionId = 2;

        Region region = new Region(regionId, "Test Region Name", "Test Description");
        Region regionToUpdate = new Region(regionId, "Updated Region Name", "Updated Description", 1, 2);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(countryRepository, climateRepository));

        when(regionRepository.existsById(regionId)).thenReturn(true);
        when(regionRepository.findById(regionId)).thenReturn(Optional.of(region));
        when(countryRepository.existsById(1)).thenReturn(false);
        when(climateRepository.existsById(2)).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> regionService.updateRegion(regionId, regionToUpdate));
    }

    @Test
    public void whenRegionIdIsNotFound_updateRegion_ThrowsIllegalArgumentException() {
        int regionId = 1;
        Region region = new Region(regionId, "Old Region Name", "Old Description");

        when(regionRepository.existsById(regionId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> regionService.updateRegion(regionId, region));
    }
}
