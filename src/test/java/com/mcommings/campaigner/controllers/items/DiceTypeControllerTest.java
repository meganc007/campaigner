package com.mcommings.campaigner.controllers.items;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.items.controllers.DiceTypeController;
import com.mcommings.campaigner.modules.items.dtos.dice_types.CreateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.UpdateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.ViewDiceTypeDTO;
import com.mcommings.campaigner.modules.items.services.DiceTypeService;
import com.mcommings.campaigner.setup.items.factories.ItemsTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiceTypeController.class)
public class DiceTypeControllerTest extends BaseControllerTest {

    @MockitoBean
    DiceTypeService diceTypeService;

    //GET all
    @Test
    void getDiceTypes_returnsList() throws Exception {

        when(diceTypeService.getAll())
                .thenReturn(List.of(ItemsTestDataFactory.viewDiceTypeDTO()));

        get("/api/dicetypes")
                .andExpect(status().isOk());

        verify(diceTypeService).getAll();
    }

    //GET by id
    @Test
    void getDiceTypes_returnsDiceTypes() throws Exception {

        ViewDiceTypeDTO dto = ItemsTestDataFactory.viewDiceTypeDTO();

        when(diceTypeService.getById(1)).thenReturn(dto);

        get("/api/dicetypes/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //POST
    @Test
    void createDiceType_returnsSaved() throws Exception {

        CreateDiceTypeDTO create =
                ItemsTestDataFactory.createDiceTypeDTO();

        ViewDiceTypeDTO response =
                ItemsTestDataFactory.viewDiceTypeDTO();

        when(diceTypeService.create(any())).thenReturn(response);

        post("/api/dicetypes", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateDiceType_returnsUpdated() throws Exception {

        UpdateDiceTypeDTO update =
                ItemsTestDataFactory.updateDiceTypeDTO();

        ViewDiceTypeDTO response =
                ItemsTestDataFactory.viewDiceTypeDTO();

        when(diceTypeService.update(any())).thenReturn(response);

        put("/api/dicetypes", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteDiceType_returnsOk() throws Exception {

        delete("/api/dicetypes/1")
                .andExpect(status().isOk());

        verify(diceTypeService).delete(1);
    }
}
