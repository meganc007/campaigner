package com.mcommings.campaigner.controllers.items;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.items.controllers.ItemTypeController;
import com.mcommings.campaigner.modules.items.dtos.item_types.CreateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.UpdateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.ViewItemTypeDTO;
import com.mcommings.campaigner.modules.items.services.ItemTypeService;
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

@WebMvcTest(ItemTypeController.class)
public class ItemTypeControllerTest extends BaseControllerTest {

    @MockitoBean
    ItemTypeService itemTypeService;

    //GET all
    @Test
    void getItemTypes_returnsList() throws Exception {

        when(itemTypeService.getAll())
                .thenReturn(List.of(ItemsTestDataFactory.viewItemTypeDTO()));

        get("/api/itemtypes")
                .andExpect(status().isOk());

        verify(itemTypeService).getAll();
    }

    //GET by id
    @Test
    void getItemTypes_returnsItemTypes() throws Exception {

        ViewItemTypeDTO dto = ItemsTestDataFactory.viewItemTypeDTO();

        when(itemTypeService.getById(1)).thenReturn(dto);

        get("/api/itemtypes/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //POST
    @Test
    void createItemType_returnsSaved() throws Exception {

        CreateItemTypeDTO create =
                ItemsTestDataFactory.createItemTypeDTO();

        ViewItemTypeDTO response =
                ItemsTestDataFactory.viewItemTypeDTO();

        when(itemTypeService.create(any())).thenReturn(response);

        post("/api/itemtypes", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateItemType_returnsUpdated() throws Exception {

        UpdateItemTypeDTO update =
                ItemsTestDataFactory.updateItemTypeDTO();

        ViewItemTypeDTO response =
                ItemsTestDataFactory.viewItemTypeDTO();

        when(itemTypeService.update(any())).thenReturn(response);

        put("/api/itemtypes", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteItemType_returnsOk() throws Exception {

        delete("/api/itemtypes/1")
                .andExpect(status().isOk());

        verify(itemTypeService).delete(1);
    }
}
