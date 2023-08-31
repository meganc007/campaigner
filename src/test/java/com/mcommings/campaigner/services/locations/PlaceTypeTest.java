package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.Place;
import com.mcommings.campaigner.models.locations.PlaceType;
import com.mcommings.campaigner.repositories.locations.IPlaceRepository;
import com.mcommings.campaigner.repositories.locations.IPlaceTypesRepository;
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

import static com.mcommings.campaigner.enums.ForeignKey.FK_PLACE_TYPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlaceTypeTest {

    @Mock
    private IPlaceTypesRepository placeTypeRepository;
    @Mock
    private IPlaceRepository placeRepository;

    @InjectMocks
    private PlaceTypeService placeTypeService;

    @Test
    public void whenThereArePlaceTypes_getPlaceTypes_ReturnsPlaceTypes() {
        List<PlaceType> placeTypes = new ArrayList<>();
        placeTypes.add(new PlaceType(1, "Place Type 1", "Description 1"));
        placeTypes.add(new PlaceType(2, "Place Type 2", "Description 2"));
        when(placeTypeRepository.findAll()).thenReturn(placeTypes);

        List<PlaceType> result = placeTypeService.getPlaceTypes();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(placeTypes, result);
    }

    @Test
    public void whenThereAreNoPlaceTypes_getPlaceTypes_ReturnsNothing() {
        List<PlaceType> placeTypes = new ArrayList<>();
        when(placeTypeRepository.findAll()).thenReturn(placeTypes);

        List<PlaceType> result = placeTypeService.getPlaceTypes();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(placeTypes, result);
    }

    @Test
    public void whenPlaceTypeIsValid_savePlaceType_SavesThePlaceType() {
        PlaceType placeType = new PlaceType(1, "PlaceType 1", "Description 1");
        when(placeTypeRepository.saveAndFlush(placeType)).thenReturn(placeType);

        assertDoesNotThrow(() -> placeTypeService.savePlaceType(placeType));
        verify(placeTypeRepository, times(1)).saveAndFlush(placeType);
    }

    @Test
    public void whenPlaceTypeNameIsInvalid_savePlaceType_ThrowsIllegalArgumentException() {
        PlaceType placeTypeWithEmptyName = new PlaceType(1, "", "Description 1");
        PlaceType placeTypeWithNullName = new PlaceType(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> placeTypeService.savePlaceType(placeTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> placeTypeService.savePlaceType(placeTypeWithNullName));
    }

    @Test
    public void whenPlaceTypeNameAlreadyExists_savePlaceType_ThrowsDataIntegrityViolationException() {
        PlaceType placeType = new PlaceType(1, "PlaceType 1", "Description 1");
        PlaceType placeTypeWithDuplicatedName = new PlaceType(2, "PlaceType 1", "Description 2");
        when(placeTypeRepository.saveAndFlush(placeType)).thenReturn(placeType);
        when(placeTypeRepository.saveAndFlush(placeTypeWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> placeTypeService.savePlaceType(placeType));
        assertThrows(DataIntegrityViolationException.class, () -> placeTypeService.savePlaceType(placeTypeWithDuplicatedName));
    }

    @Test
    public void whenPlaceTypeIdExists_deletePlaceType_DeletesThePlaceType() {
        int placeTypeId = 1;
        when(placeTypeRepository.existsById(placeTypeId)).thenReturn(true);
        assertDoesNotThrow(() -> placeTypeService.deletePlaceType(placeTypeId));
        verify(placeTypeRepository, times(1)).deleteById(placeTypeId);
    }

    @Test
    public void whenPlaceTypeIdDoesNotExist_deletePlaceType_ThrowsIllegalArgumentException() {
        int placeTypeId = 9000;
        when(placeTypeRepository.existsById(placeTypeId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> placeTypeService.deletePlaceType(placeTypeId));
    }

    @Test
    public void whenPlaceTypeIdIsAForeignKey_deletePlaceType_ThrowsDataIntegrityViolationException() {
        int placeTypeId = 1;
        Place place = new Place(1, "Place", "Description", placeTypeId, 1, 1, 1);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(placeRepository));
        List<Place> places = new ArrayList<>(Arrays.asList(place));

        when(placeTypeRepository.existsById(placeTypeId)).thenReturn(true);
        when(placeRepository.findByfk_place_type(placeTypeId)).thenReturn(places);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_PLACE_TYPE.columnName, placeTypeId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> placeTypeService.deletePlaceType(placeTypeId));
    }
    @Test
    public void whenPlaceTypeIdIsFound_updatePlaceType_UpdatesThePlaceType() {
        int placeTypeId = 1;
        PlaceType placeType = new PlaceType(placeTypeId, "Old PlaceType Name", "Old Description");
        PlaceType placeTypeToUpdate = new PlaceType(placeTypeId, "Updated PlaceType Name", "Updated Description");

        when(placeTypeRepository.existsById(placeTypeId)).thenReturn(true);
        when(placeTypeRepository.findById(placeTypeId)).thenReturn(Optional.of(placeType));

        placeTypeService.updatePlaceType(placeTypeId, placeTypeToUpdate);

        verify(placeTypeRepository).findById(placeTypeId);

        PlaceType result = placeTypeRepository.findById(placeTypeId).get();
        Assertions.assertEquals(placeTypeToUpdate.getName(), result.getName());
        Assertions.assertEquals(placeTypeToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenPlaceTypeIdIsNotFound_updatePlaceType_ThrowsIllegalArgumentException() {
        int placeTypeId = 1;
        PlaceType placeType = new PlaceType(placeTypeId, "Old PlaceType Name", "Old Description");

        when(placeTypeRepository.existsById(placeTypeId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> placeTypeService.updatePlaceType(placeTypeId, placeType));
    }
}
