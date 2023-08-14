package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.PlaceType;
import com.mcommings.campaigner.models.repositories.IPlaceTypesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class PlaceTypeTest {

    @Mock
    private IPlaceTypesRepository placeTypesRepository;

    @InjectMocks
    private PlaceTypeService placeTypeService;

    @Test
    public void whenThereArePlaceTypes_getPlaceTypes_ReturnsPlaceTypes() {
        List<PlaceType> placeTypes = new ArrayList<>();
        placeTypes.add(new PlaceType(1, "Place Type 1", "Description 1"));
        placeTypes.add(new PlaceType(2, "Place Type 2", "Description 2"));
        when(placeTypesRepository.findAll()).thenReturn(placeTypes);

        List<PlaceType> result = placeTypeService.getPlaceTypes();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(placeTypes, result);
    }

    @Test
    public void whenThereAreNoPlaceTypes_getPlaceTypes_ReturnsNothing() {
        List<PlaceType> placeTypes = new ArrayList<>();
        when(placeTypesRepository.findAll()).thenReturn(placeTypes);

        List<PlaceType> result = placeTypeService.getPlaceTypes();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(placeTypes, result);

    }
}
