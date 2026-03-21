package com.mcommings.campaigner.controllers.common;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.common.controllers.WealthController;
import com.mcommings.campaigner.modules.common.dtos.wealth.CreateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.UpdateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.ViewWealthDTO;
import com.mcommings.campaigner.modules.common.services.WealthService;
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

@WebMvcTest(WealthController.class)
public class WealthControllerTest extends BaseControllerTest {

    @MockitoBean
    WealthService wealthService;

    //GET all
    @Test
    void getWealths_returnsList() throws Exception {

        when(wealthService.getAll())
                .thenReturn(List.of(CommonTestDataFactory.viewWealthDTO()));

        get("/api/wealth")
                .andExpect(status().isOk());

        verify(wealthService).getAll();
    }

    //GET by id
    @Test
    void getWealth_returnsWealth() throws Exception {

        ViewWealthDTO dto = CommonTestDataFactory.viewWealthDTO();

        when(wealthService.getById(1)).thenReturn(dto);

        get("/api/wealth/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //POST
    @Test
    void createWealth_returnsSaved() throws Exception {

        CreateWealthDTO create =
                CommonTestDataFactory.createWealthDTO();

        ViewWealthDTO response =
                CommonTestDataFactory.viewWealthDTO();

        when(wealthService.create(any())).thenReturn(response);

        post("/api/wealth", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateWealth_returnsUpdated() throws Exception {

        UpdateWealthDTO update =
                CommonTestDataFactory.updateWealthDTO();

        ViewWealthDTO response =
                CommonTestDataFactory.viewWealthDTO();

        when(wealthService.update(any())).thenReturn(response);

        put("/api/wealth", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteWealth_returnsOk() throws Exception {

        delete("/api/wealth/1")
                .andExpect(status().isOk());

        verify(wealthService).delete(1);
    }
}
