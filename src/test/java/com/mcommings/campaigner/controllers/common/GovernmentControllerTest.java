package com.mcommings.campaigner.controllers.common;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.common.controllers.GovernmentController;
import com.mcommings.campaigner.modules.common.dtos.government.CreateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.UpdateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.ViewGovernmentDTO;
import com.mcommings.campaigner.modules.common.services.GovernmentService;
import com.mcommings.campaigner.setup.common.factories.CommonTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GovernmentController.class)
public class GovernmentControllerTest extends BaseControllerTest {

    @MockitoBean
    GovernmentService governmentService;

    //GET all
    @Test
    void getGovernments_returnsList() throws Exception {

        when(governmentService.getAll())
                .thenReturn(List.of(CommonTestDataFactory.viewGovernmentDTO()));

        get("/api/governments")
                .andExpect(status().isOk());

        verify(governmentService).getAll();
    }

    //GET by id
    @Test
    void getGovernment_returnsGovernment() throws Exception {

        ViewGovernmentDTO dto = CommonTestDataFactory.viewGovernmentDTO();

        when(governmentService.getById(1)).thenReturn(dto);

        get("/api/governments/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //POST
    @Test
    void createGovernment_returnsSaved() throws Exception {

        CreateGovernmentDTO create =
                CommonTestDataFactory.createGovernmentDTO();

        ViewGovernmentDTO response =
                CommonTestDataFactory.viewGovernmentDTO();

        when(governmentService.create(any())).thenReturn(response);

        post("/api/governments", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateGovernment_returnsUpdated() throws Exception {

        UpdateGovernmentDTO update =
                CommonTestDataFactory.updateGovernmentDTO();

        ViewGovernmentDTO response =
                CommonTestDataFactory.viewGovernmentDTO();

        when(governmentService.update(any())).thenReturn(response);

        put("/api/governments", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteGovernment_returnsOk() throws Exception {

        delete("/api/governments/1")
                .andExpect(status().isOk());

        verify(governmentService).delete(1);
    }
}
