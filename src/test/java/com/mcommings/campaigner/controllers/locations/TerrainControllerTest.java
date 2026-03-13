package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.locations.controllers.TerrainController;
import com.mcommings.campaigner.modules.locations.dtos.terrains.CreateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.UpdateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.ViewTerrainDTO;
import com.mcommings.campaigner.modules.locations.services.TerrainService;
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

@WebMvcTest(TerrainController.class)
public class TerrainControllerTest extends BaseControllerTest {

    @MockitoBean
    TerrainService terrainService;

    //GET all
    @Test
    void getTerrains_returnsList() throws Exception {

        when(terrainService.getAll())
                .thenReturn(List.of(LocationsTestDataFactory.viewTerrainDTO()));

        get("/api/terrains")
                .andExpect(status().isOk());

        verify(terrainService).getAll();
    }

    //GET by id
    @Test
    void getTerrains_returnsTerrains() throws Exception {

        ViewTerrainDTO dto = LocationsTestDataFactory.viewTerrainDTO();

        when(terrainService.getById(1)).thenReturn(dto);

        get("/api/terrains/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //POST
    @Test
    void createTerrain_returnsSaved() throws Exception {

        CreateTerrainDTO create =
                LocationsTestDataFactory.createTerrainDTO();

        ViewTerrainDTO response =
                LocationsTestDataFactory.viewTerrainDTO();

        when(terrainService.create(any())).thenReturn(response);

        post("/api/terrains", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateTerrain_returnsUpdated() throws Exception {

        UpdateTerrainDTO update =
                LocationsTestDataFactory.updateTerrainDTO();

        ViewTerrainDTO response =
                LocationsTestDataFactory.viewTerrainDTO();

        when(terrainService.update(any())).thenReturn(response);

        put("/api/terrains", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteTerrain_returnsOk() throws Exception {

        delete("/api/terrains/1")
                .andExpect(status().isOk());

        verify(terrainService).delete(1);
    }
}
