package com.mcommings.campaigner.controllers.items;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.items.controllers.ItemController;
import com.mcommings.campaigner.modules.items.dtos.items.CreateItemDTO;
import com.mcommings.campaigner.modules.items.dtos.items.UpdateItemDTO;
import com.mcommings.campaigner.modules.items.dtos.items.ViewItemDTO;
import com.mcommings.campaigner.modules.items.services.ItemService;
import com.mcommings.campaigner.setup.items.factories.ItemsTestDataFactory;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest extends BaseControllerTest {

    @MockitoBean
    ItemService itemService;

    //GET all
    @Test
    void getItems_returnsList() throws Exception {

        when(itemService.getAll())
                .thenReturn(List.of(ItemsTestDataFactory.viewItemDTO()));

        get("/api/items")
                .andExpect(status().isOk());

        verify(itemService).getAll();
    }

    //GET by id
    @Test
    void getItem_returnsItem() throws Exception {

        ViewItemDTO dto = ItemsTestDataFactory.viewItemDTO();

        when(itemService.getById(1)).thenReturn(dto);

        get("/api/items/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by ItemTypeId
    @Test
    void getItemsByItemTypeId_returnsItems() throws Exception {

        when(itemService.getItemsByItemTypeId(ItemsTestConstants.ITEM_TYPE_ID))
                .thenReturn(List.of(ItemsTestDataFactory.viewItemDTO()));

        get("/api/items/itemType/" + ItemsTestConstants.ITEM_TYPE_ID)
                .andExpect(status().isOk());

        verify(itemService)
                .getItemsByItemTypeId(ItemsTestConstants.ITEM_TYPE_ID);
    }

    //GET by CampaignUUID
    @Test
    void getItemsByCampaignUUID_returnsItems() throws Exception {

        when(itemService.getItemsByCampaignUUID(ItemsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(ItemsTestDataFactory.viewItemDTO()));

        get("/api/items/campaign/" + ItemsTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(itemService)
                .getItemsByCampaignUUID(ItemsTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createItem_returnsSaved() throws Exception {

        CreateItemDTO create =
                ItemsTestDataFactory.createItemDTO();

        ViewItemDTO response =
                ItemsTestDataFactory.viewItemDTO();

        when(itemService.create(any())).thenReturn(response);

        post("/api/items", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateItem_returnsUpdated() throws Exception {

        UpdateItemDTO update =
                ItemsTestDataFactory.updateItemDTO();

        ViewItemDTO response =
                ItemsTestDataFactory.viewItemDTO();

        when(itemService.update(any())).thenReturn(response);

        put("/api/items", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteItem_returnsOk() throws Exception {

        delete("/api/items/1")
                .andExpect(status().isOk());

        verify(itemService).delete(1);
    }
}
