package com.mcommings.campaigner.controllers.items;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.items.controllers.WeaponTypeController;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.CreateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.UpdateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.ViewWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.services.WeaponTypeService;
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

@WebMvcTest(WeaponTypeController.class)
public class WeaponTypeControllerTest extends BaseControllerTest {

    @MockitoBean
    WeaponTypeService weaponTypeService;

    //GET all
    @Test
    void getWeaponTypes_returnsList() throws Exception {

        when(weaponTypeService.getAll())
                .thenReturn(List.of(ItemsTestDataFactory.viewWeaponTypeDTO()));

        get("/api/weapontypes")
                .andExpect(status().isOk());

        verify(weaponTypeService).getAll();
    }

    //GET by id
    @Test
    void getWeaponTypes_returnsWeaponTypes() throws Exception {

        ViewWeaponTypeDTO dto = ItemsTestDataFactory.viewWeaponTypeDTO();

        when(weaponTypeService.getById(1)).thenReturn(dto);

        get("/api/weapontypes/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //POST
    @Test
    void createWeaponType_returnsSaved() throws Exception {

        CreateWeaponTypeDTO create =
                ItemsTestDataFactory.createWeaponTypeDTO();

        ViewWeaponTypeDTO response =
                ItemsTestDataFactory.viewWeaponTypeDTO();

        when(weaponTypeService.create(any())).thenReturn(response);

        post("/api/weapontypes", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateWeaponType_returnsUpdated() throws Exception {

        UpdateWeaponTypeDTO update =
                ItemsTestDataFactory.updateWeaponTypeDTO();

        ViewWeaponTypeDTO response =
                ItemsTestDataFactory.viewWeaponTypeDTO();

        when(weaponTypeService.update(any())).thenReturn(response);

        put("/api/weapontypes", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteWeaponType_returnsOk() throws Exception {

        delete("/api/weapontypes/1")
                .andExpect(status().isOk());

        verify(weaponTypeService).delete(1);
    }
}
