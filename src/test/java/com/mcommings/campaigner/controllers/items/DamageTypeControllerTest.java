package com.mcommings.campaigner.controllers.items;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.items.controllers.DamageTypeController;
import com.mcommings.campaigner.modules.items.dtos.damage_types.CreateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.UpdateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.ViewDamageTypeDTO;
import com.mcommings.campaigner.modules.items.services.DamageTypeService;
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

@WebMvcTest(DamageTypeController.class)
class DamageTypeControllerTest extends BaseControllerTest {

    @MockitoBean
    DamageTypeService damageTypeService;

    //GET all
    @Test
    void getDamageTypes_returnsList() throws Exception {

        when(damageTypeService.getAll())
                .thenReturn(List.of(ItemsTestDataFactory.viewDamageTypeDTO()));

        get("/api/damagetypes")
                .andExpect(status().isOk());

        verify(damageTypeService).getAll();
    }

    //GET by id
    @Test
    void getDamageTypes_returnsDamageTypes() throws Exception {

        ViewDamageTypeDTO dto = ItemsTestDataFactory.viewDamageTypeDTO();

        when(damageTypeService.getById(1)).thenReturn(dto);

        get("/api/damagetypes/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //POST
    @Test
    void createDamageType_returnsSaved() throws Exception {

        CreateDamageTypeDTO create =
                ItemsTestDataFactory.createDamageTypeDTO();

        ViewDamageTypeDTO response =
                ItemsTestDataFactory.viewDamageTypeDTO();

        when(damageTypeService.create(any())).thenReturn(response);

        post("/api/damagetypes", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateDamageType_returnsUpdated() throws Exception {

        UpdateDamageTypeDTO update =
                ItemsTestDataFactory.updateDamageTypeDTO();

        ViewDamageTypeDTO response =
                ItemsTestDataFactory.viewDamageTypeDTO();

        when(damageTypeService.update(any())).thenReturn(response);

        put("/api/damagetypes", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteDamageType_returnsOk() throws Exception {

        delete("/api/damagetypes/1")
                .andExpect(status().isOk());

        verify(damageTypeService).delete(1);
    }
}
