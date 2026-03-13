package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.locations.controllers.PlaceTypeController;
import com.mcommings.campaigner.modules.locations.dtos.place_types.CreatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.UpdatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.ViewPlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.services.PlaceTypeService;
import com.mcommings.campaigner.setup.locations.factories.LocationsTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaceTypeController.class)
public class PlaceTypeControllerTest extends BaseControllerTest {

    @MockitoBean
    PlaceTypeService placeTypeService;

    //GET all
    @Test
    void getPlaceTypes_returnsList() throws Exception {

        when(placeTypeService.getAll())
                .thenReturn(List.of(LocationsTestDataFactory.viewPlaceTypeDTO()));

        get("/api/placetypes")
                .andExpect(status().isOk());

        verify(placeTypeService).getAll();
    }

    //GET by id
    @Test
    void getPlaceTypes_returnsPlaceTypes() throws Exception {

        ViewPlaceTypeDTO dto = LocationsTestDataFactory.viewPlaceTypeDTO();

        when(placeTypeService.getById(1)).thenReturn(dto);

        get("/api/placetypes/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //POST
    @Test
    void createPlaceType_returnsSaved() throws Exception {

        CreatePlaceTypeDTO create =
                LocationsTestDataFactory.createPlaceTypeDTO();

        ViewPlaceTypeDTO response =
                LocationsTestDataFactory.viewPlaceTypeDTO();

        when(placeTypeService.create(any())).thenReturn(response);

        post("/api/placetypes", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updatePlaceType_returnsUpdated() throws Exception {

        UpdatePlaceTypeDTO update =
                LocationsTestDataFactory.updatePlaceTypeDTO();

        ViewPlaceTypeDTO response =
                LocationsTestDataFactory.viewPlaceTypeDTO();

        when(placeTypeService.update(any())).thenReturn(response);

        put("/api/placetypes", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deletePlaceType_returnsOk() throws Exception {

        delete("/api/placetypes/1")
                .andExpect(status().isOk());

        verify(placeTypeService).delete(1);
    }
}
