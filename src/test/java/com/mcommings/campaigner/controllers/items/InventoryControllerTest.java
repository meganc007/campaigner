package com.mcommings.campaigner.controllers.items;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.items.controllers.InventoryController;
import com.mcommings.campaigner.modules.items.dtos.inventories.CreateInventoryDTO;
import com.mcommings.campaigner.modules.items.dtos.inventories.UpdateInventoryDTO;
import com.mcommings.campaigner.modules.items.dtos.inventories.ViewInventoryDTO;
import com.mcommings.campaigner.modules.items.services.InventoryService;
import com.mcommings.campaigner.setup.items.factories.ItemsTestDataFactory;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
public class InventoryControllerTest extends BaseControllerTest {

    @MockitoBean
    InventoryService inventoryService;

    //GET all
    @Test
    void getInventories_returnsList() throws Exception {

        when(inventoryService.getAll())
                .thenReturn(List.of(ItemsTestDataFactory.viewInventoryDTO()));

        get("/api/inventory")
                .andExpect(status().isOk());

        verify(inventoryService).getAll();
    }

    //GET by id
    @Test
    void getInventory_returnsInventory() throws Exception {

        ViewInventoryDTO dto = ItemsTestDataFactory.viewInventoryDTO();

        when(inventoryService.getById(1)).thenReturn(dto);

        get("/api/inventory/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by PersonId
    @Test
    void getInventoriesByPersonId_returnsInventories() throws Exception {

        when(inventoryService.getInventoriesByPersonId(ItemsTestConstants.PERSON_ID))
                .thenReturn(List.of(ItemsTestDataFactory.viewInventoryDTO()));

        get("/api/inventory/person/" + ItemsTestConstants.PERSON_ID)
                .andExpect(status().isOk());

        verify(inventoryService)
                .getInventoriesByPersonId(ItemsTestConstants.PERSON_ID);
    }

    //GET by ItemId
    @Test
    void getInventoriesByItemId_returnsInventories() throws Exception {

        when(inventoryService.getInventoriesByItemId(ItemsTestConstants.ITEM_ID))
                .thenReturn(List.of(ItemsTestDataFactory.viewInventoryDTO()));

        get("/api/inventory/item/" + ItemsTestConstants.ITEM_ID)
                .andExpect(status().isOk());

        verify(inventoryService)
                .getInventoriesByItemId(ItemsTestConstants.ITEM_ID);
    }

    //GET by WeaponId
    @Test
    void getInventoriesByWeaponId_returnsInventories() throws Exception {

        when(inventoryService.getInventoriesByWeaponId(ItemsTestConstants.WEAPON_ID))
                .thenReturn(List.of(ItemsTestDataFactory.viewInventoryDTO()));

        get("/api/inventory/weapon/" + ItemsTestConstants.WEAPON_ID)
                .andExpect(status().isOk());

        verify(inventoryService)
                .getInventoriesByWeaponId(ItemsTestConstants.WEAPON_ID);
    }

    //GET by PlaceId
    @Test
    void getInventoriesByPlaceId_returnsInventories() throws Exception {

        when(inventoryService.getInventoriesByPlaceId(LocationsTestConstants.PLACE_ID))
                .thenReturn(List.of(ItemsTestDataFactory.viewInventoryDTO()));

        get("/api/inventory/place/" + LocationsTestConstants.PLACE_ID)
                .andExpect(status().isOk());

        verify(inventoryService)
                .getInventoriesByPlaceId(LocationsTestConstants.PLACE_ID);
    }

    //GET by CampaignUUID
    @Test
    void getInventoriesByCampaignUUID_returnsInventorys() throws Exception {

        when(inventoryService.getInventoriesByCampaignUUID(ItemsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(ItemsTestDataFactory.viewInventoryDTO()));

        get("/api/inventory/campaign/" + ItemsTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(inventoryService)
                .getInventoriesByCampaignUUID(ItemsTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createInventory_returnsSaved() throws Exception {

        CreateInventoryDTO create =
                ItemsTestDataFactory.createInventoryDTO();

        ViewInventoryDTO response =
                ItemsTestDataFactory.viewInventoryDTO();

        when(inventoryService.create(any())).thenReturn(response);

        post("/api/inventory", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateInventory_returnsUpdated() throws Exception {

        UpdateInventoryDTO update =
                ItemsTestDataFactory.updateInventoryDTO();

        ViewInventoryDTO response =
                ItemsTestDataFactory.viewInventoryDTO();

        when(inventoryService.update(any())).thenReturn(response);

        put("/api/inventory", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteInventory_returnsOk() throws Exception {

        delete("/api/inventory/1")
                .andExpect(status().isOk());

        verify(inventoryService).delete(1);
    }
}
