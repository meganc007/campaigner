package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.locations.controllers.SettlementTypeController;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.CreateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.UpdateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.ViewSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.services.SettlementTypeService;
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

@WebMvcTest(SettlementTypeController.class)
public class SettlementTypeControllerTest extends BaseControllerTest {

    @MockitoBean
    SettlementTypeService settlementTypeService;

    //GET all
    @Test
    void getSettlementTypes_returnsList() throws Exception {

        when(settlementTypeService.getAll())
                .thenReturn(List.of(LocationsTestDataFactory.viewSettlementTypeDTO()));

        get("/api/settlementtypes")
                .andExpect(status().isOk());

        verify(settlementTypeService).getAll();
    }

    //GET by id
    @Test
    void getSettlementTypes_returnsSettlementTypes() throws Exception {

        ViewSettlementTypeDTO dto = LocationsTestDataFactory.viewSettlementTypeDTO();

        when(settlementTypeService.getById(1)).thenReturn(dto);

        get("/api/settlementtypes/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //POST
    @Test
    void createSettlementType_returnsSaved() throws Exception {

        CreateSettlementTypeDTO create =
                LocationsTestDataFactory.createSettlementTypeDTO();

        ViewSettlementTypeDTO response =
                LocationsTestDataFactory.viewSettlementTypeDTO();

        when(settlementTypeService.create(any())).thenReturn(response);

        post("/api/settlementtypes", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateSettlementType_returnsUpdated() throws Exception {

        UpdateSettlementTypeDTO update =
                LocationsTestDataFactory.updateSettlementTypeDTO();

        ViewSettlementTypeDTO response =
                LocationsTestDataFactory.viewSettlementTypeDTO();

        when(settlementTypeService.update(any())).thenReturn(response);

        put("/api/settlementtypes", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteSettlementType_returnsOk() throws Exception {

        delete("/api/settlementtypes/1")
                .andExpect(status().isOk());

        verify(settlementTypeService).delete(1);
    }
}
