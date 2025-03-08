package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.locations.Place;
import com.mcommings.campaigner.entities.people.EventPlacePerson;
import com.mcommings.campaigner.repositories.locations.*;
import com.mcommings.campaigner.repositories.people.IEventPlacePersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_PLACE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlaceTest {

    @Mock
    private IPlaceRepository placeRepository;
    @Mock
    private IPlaceTypesRepository placeTypesRepository;
    @Mock
    private ITerrainRepository terrainRepository;
    @Mock
    private ICountryRepository countryRepository;
    @Mock
    private ICityRepository cityRepository;
    @Mock
    private IRegionRepository regionRepository;
    @Mock
    private IEventPlacePersonRepository eventPlacePersonRepository;

    @InjectMocks
    private PlaceService placeService;

    @Test
    public void whenThereArePlaces_getPlaces_ReturnsPlaces() {
        List<Place> places = new ArrayList<>();
        places.add(new Place(1, "Place 1", "Description 1", UUID.randomUUID()));
        places.add(new Place(2, "Place 2", "Description 2", UUID.randomUUID()));
        places.add(new Place(3, "Place 3", "Description 3", UUID.randomUUID(), 1, 2, 3, 4, 5));
        when(placeRepository.findAll()).thenReturn(places);

        List<Place> result = placeService.getPlaces();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(places, result);
    }

    @Test
    public void whenThereAreNoPlaces_getPlaces_ReturnsNothing() {
        List<Place> places = new ArrayList<>();
        when(placeRepository.findAll()).thenReturn(places);

        List<Place> result = placeService.getPlaces();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(places, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getPlacesByCampaignUUID_ReturnsPlaces() {
        UUID campaign = UUID.randomUUID();
        List<Place> places = new ArrayList<>();
        places.add(new Place(1, "Place 1", "Description 1", campaign));
        places.add(new Place(2, "Place 2", "Description 2", campaign));
        places.add(new Place(3, "Place 3", "Description 3", campaign, 1, 2, 3, 4, 5));
        when(placeRepository.findByfk_campaign_uuid(campaign)).thenReturn(places);

        List<Place> results = placeService.getPlacesByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(places, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getPlacesByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Place> places = new ArrayList<>();
        when(placeRepository.findByfk_campaign_uuid(campaign)).thenReturn(places);

        List<Place> result = placeService.getPlacesByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(places, result);
    }

    @Test
    public void whenPlaceWithNoForeignKeysIsValid_savePlace_SavesThePlace() {
        Place place = new Place(1, "Place 1", "Description 1", UUID.randomUUID());
        when(placeRepository.saveAndFlush(place)).thenReturn(place);

        assertDoesNotThrow(() -> placeService.savePlace(place));

        verify(placeRepository, times(1)).saveAndFlush(place);
    }

    @Test
    public void whenPlaceWithForeignKeysIsValid_savePlace_SavesThePlace() {
        Place place = new Place(1, "Place 1", "Description 1", UUID.randomUUID(), 1, 2, 3, 4, 5);

        when(placeTypesRepository.existsById(1)).thenReturn(true);
        when(terrainRepository.existsById(2)).thenReturn(true);
        when(countryRepository.existsById(3)).thenReturn(true);
        when(cityRepository.existsById(4)).thenReturn(true);
        when(regionRepository.existsById(5)).thenReturn(true);
        when(placeRepository.saveAndFlush(place)).thenReturn(place);

        assertDoesNotThrow(() -> placeService.savePlace(place));

        verify(placeRepository, times(1)).saveAndFlush(place);
    }

    @Test
    public void whenPlaceNameIsInvalid_savePlace_ThrowsIllegalArgumentException() {
        Place placeWithEmptyName = new Place(1, "", "Description 1", UUID.randomUUID());
        Place placeWithNullName = new Place(2, null, "Description 2", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> placeService.savePlace(placeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> placeService.savePlace(placeWithNullName));
    }

    @Test
    public void whenPlaceNameAlreadyExists_savePlace_ThrowsDataIntegrityViolationException() {
        Place place = new Place(1, "Place 1", "Description 1", UUID.randomUUID(), 1, 2, 3, 4, 5);
        Place placeWithDuplicatedName = new Place(2, "Place 1", "Description 2", UUID.randomUUID(), 5, 6, 7, 8, 9);

        when(placeTypesRepository.existsById(1)).thenReturn(true);
        when(terrainRepository.existsById(2)).thenReturn(true);
        when(countryRepository.existsById(3)).thenReturn(true);
        when(cityRepository.existsById(4)).thenReturn(true);
        when(regionRepository.existsById(5)).thenReturn(true);

        when(placeTypesRepository.existsById(5)).thenReturn(true);
        when(terrainRepository.existsById(6)).thenReturn(true);
        when(countryRepository.existsById(7)).thenReturn(true);
        when(cityRepository.existsById(8)).thenReturn(true);
        when(regionRepository.existsById(9)).thenReturn(true);

        when(placeRepository.saveAndFlush(place)).thenReturn(place);
        when(placeRepository.saveAndFlush(placeWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> placeService.savePlace(place));
        assertThrows(DataIntegrityViolationException.class, () -> placeService.savePlace(placeWithDuplicatedName));
    }

    @Test
    public void whenPlaceHasInvalidForeignKeys_savePlace_ThrowsDataIntegrityViolationException() {
        Place place = new Place(1, "Place 1", "Description 1", UUID.randomUUID(), 1, 2, 3, 4, 5);

        when(placeTypesRepository.existsById(1)).thenReturn(true);
        when(terrainRepository.existsById(2)).thenReturn(false);
        when(placeRepository.saveAndFlush(place)).thenReturn(place);

        assertThrows(DataIntegrityViolationException.class, () -> placeService.savePlace(place));

    }

    @Test
    public void whenPlaceIdExists_deletePlace_DeletesThePlace() {
        int placeId = 1;
        when(placeRepository.existsById(placeId)).thenReturn(true);
        assertDoesNotThrow(() -> placeService.deletePlace(placeId));
        verify(placeRepository, times(1)).deleteById(placeId);
    }

    @Test
    public void whenPlaceIdDoesNotExist_deletePlace_ThrowsIllegalArgumentException() {
        int placeId = 9000;
        when(placeRepository.existsById(placeId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> placeService.deletePlace(placeId));
    }

    @Test
    public void whenPlaceIdIsAForeignKey_deletePlace_ThrowsDataIntegrityViolationException() {
        int placeId = 1;
        EventPlacePerson epp = new EventPlacePerson(1, 1, placeId, 1, UUID.randomUUID());
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(eventPlacePersonRepository));
        List<EventPlacePerson> epps = new ArrayList<>(Arrays.asList(epp));

        when(placeRepository.existsById(placeId)).thenReturn(true);
        when(eventPlacePersonRepository.findByfk_place(placeId)).thenReturn(epps);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_PLACE.columnName, placeId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> placeService.deletePlace(placeId));
    }

    @Test
    public void whenPlaceIdWithNoFKIsFound_updatePlace_UpdatesThePlace() {
        int placeId = 1;
        UUID campaign = UUID.randomUUID();

        Place place = new Place(placeId, "Old Place Name", "Old Description", campaign);
        Place placeToUpdate = new Place(placeId, "Updated Place Name", "Updated Description", campaign);

        when(placeRepository.existsById(placeId)).thenReturn(true);
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

        placeService.updatePlace(placeId, placeToUpdate);

        verify(placeRepository).findById(placeId);

        Place result = placeRepository.findById(placeId).get();
        Assertions.assertEquals(placeToUpdate.getName(), result.getName());
        Assertions.assertEquals(placeToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenPlaceIdWithValidFKIsFound_updatePlace_UpdatesThePlace() {
        int placeId = 2;
        UUID campagin = UUID.randomUUID();

        Place place = new Place(placeId, "Test Place Name", "Test Description", campagin);
        Place placeToUpdate = new Place(placeId, "Updated Place Name", "Updated Description", campagin, 1, 2, 3, 4, 5);

        when(placeRepository.existsById(placeId)).thenReturn(true);
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));
        when(placeTypesRepository.existsById(1)).thenReturn(true);
        when(terrainRepository.existsById(2)).thenReturn(true);
        when(countryRepository.existsById(3)).thenReturn(true);
        when(cityRepository.existsById(4)).thenReturn(true);
        when(regionRepository.existsById(5)).thenReturn(true);

        placeService.updatePlace(placeId, placeToUpdate);

        verify(placeRepository).findById(placeId);

        Place result = placeRepository.findById(placeId).get();
        Assertions.assertEquals(placeToUpdate.getName(), result.getName());
        Assertions.assertEquals(placeToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(placeToUpdate.getFk_place_type(), result.getFk_place_type());
        Assertions.assertEquals(placeToUpdate.getFk_terrain(), result.getFk_terrain());
        Assertions.assertEquals(placeToUpdate.getFk_country(), result.getFk_country());
        Assertions.assertEquals(placeToUpdate.getFk_city(), result.getFk_city());
        Assertions.assertEquals(placeToUpdate.getFk_region(), result.getFk_region());
    }

    @Test
    public void whenPlaceIdWithInvalidFKIsFound_updatePlace_ThrowsDataIntegrityViolationException() {
        int placeId = 1;
        UUID campaign = UUID.randomUUID();

        Place place = new Place(placeId, "Test Place Name", "Test Description", campaign);
        Place placeToUpdate = new Place(placeId, "Updated Place Name", "Updated Description", campaign, 1, 2, 3, 4, 5);
        when(placeRepository.existsById(placeId)).thenReturn(true);
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));
        when(placeTypesRepository.existsById(1)).thenReturn(true);
        when(terrainRepository.existsById(2)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> placeService.updatePlace(placeId, placeToUpdate));
    }

    @Test
    public void whenPlaceIdIsNotFound_updatePlace_ThrowsIllegalArgumentException() {
        int placeId = 1;
        Place place = new Place(placeId, "Old Place Name", "Old Description", UUID.randomUUID());

        when(placeRepository.existsById(placeId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> placeService.updatePlace(placeId, place));
    }

    @Test
    public void whenSomePlaceFieldsChanged_updatePlace_OnlyUpdatesChangedFields() {
        int placeId = 1;
        Place place = new Place(1, "Place 1", "Description 1", UUID.randomUUID(),
                1, 2, 3, 4, 5);

        String newDescription = "New description";
        int newTerrain = 3;

        Place placeToUpdate = new Place();
        placeToUpdate.setDescription(newDescription);
        placeToUpdate.setFk_terrain(newTerrain);

        when(placeRepository.existsById(placeId)).thenReturn(true);
        when(placeTypesRepository.existsById(1)).thenReturn(true);
        when(terrainRepository.existsById(2)).thenReturn(true);
        when(terrainRepository.existsById(newTerrain)).thenReturn(true);
        when(countryRepository.existsById(3)).thenReturn(true);
        when(cityRepository.existsById(4)).thenReturn(true);
        when(regionRepository.existsById(5)).thenReturn(true);
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

        placeService.updatePlace(placeId, placeToUpdate);

        verify(placeRepository).findById(placeId);

        Place result = placeRepository.findById(placeId).get();
        Assertions.assertEquals(place.getName(), result.getName());
        Assertions.assertEquals(newDescription, result.getDescription());
        Assertions.assertEquals(place.getFk_place_type(), result.getFk_place_type());
        Assertions.assertEquals(newTerrain, result.getFk_terrain());
        Assertions.assertEquals(place.getFk_country(), result.getFk_country());
        Assertions.assertEquals(place.getFk_city(), result.getFk_city());
        Assertions.assertEquals(place.getFk_region(), result.getFk_region());
    }
}
