package com.mcommings.campaigner.controllers.items;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.items.controllers.InventoryController;
import com.mcommings.campaigner.modules.items.dtos.InventoryDTO;
import com.mcommings.campaigner.modules.items.entities.Inventory;
import com.mcommings.campaigner.modules.items.services.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    InventoryService inventoryService;

    private static final int VALID_INVENTORY_ID = 1;
    private static final int INVALID_INVENTORY_ID = 999;
    private static final String URI = "/api/inventory";
    private Inventory entity;
    private InventoryDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Inventory();
        entity.setId(VALID_INVENTORY_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_person(random.nextInt(100) + 1);
        entity.setFk_item(random.nextInt(100) + 1);
        entity.setFk_weapon(random.nextInt(100) + 1);
        entity.setFk_place(random.nextInt(100) + 1);

        dto = new InventoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_person(entity.getFk_person());
        dto.setFk_item(entity.getFk_item());
        dto.setFk_weapon(entity.getFk_weapon());
        dto.setFk_place(entity.getFk_place());
    }

    @Test
    void whenThereAreInventories_getInventories_ReturnsInventories() throws Exception {
        when(inventoryService.getInventories()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreInventories_getInventories_ReturnsEmptyList() throws Exception {
        when(inventoryService.getInventories()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAInventory_getInventory_ReturnsInventory() throws Exception {
        when(inventoryService.getInventory(VALID_INVENTORY_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_INVENTORY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_INVENTORY_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAInventory_getInventory_ThrowsIllegalArgumentException() throws Exception {
        when(inventoryService.getInventory(INVALID_INVENTORY_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_INVENTORY_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getInventory_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getInventory_ReturnsInternalServerError() throws Exception {
        when(inventoryService.getInventory(VALID_INVENTORY_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_INVENTORY_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getInventory_ReturnsInternalServerError() throws Exception {
        when(inventoryService.getInventory(VALID_INVENTORY_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_INVENTORY_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getInventoriesByCampaignUUID_ReturnsInventories() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(inventoryService.getInventoriesByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getInventoriesByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(inventoryService.getInventoriesByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getInventory_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPersonIDIsValid_getInventorysByPerson_ReturnsInventorys() throws Exception {
        int personId = random.nextInt(100) + 1;
        when(inventoryService.getInventoriesByPerson(personId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/person/" + personId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenPersonIDIsNotValid_getInventorysByPerson_ReturnsEmptyList() throws Exception {
        int personId = random.nextInt(100) + 1;
        when(inventoryService.getInventoriesByPerson(personId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/person/" + personId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenItemIDIsValid_getInventorysByItem_ReturnsInventorys() throws Exception {
        int itemId = random.nextInt(100) + 1;
        when(inventoryService.getInventoriesByItem(itemId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/item/" + itemId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenItemIDIsNotValid_getInventorysByItem_ReturnsEmptyList() throws Exception {
        int itemId = random.nextInt(100) + 1;
        when(inventoryService.getInventoriesByItem(itemId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/item/" + itemId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenWeaponIDIsValid_getInventorysByWeapon_ReturnsInventorys() throws Exception {
        int weaponId = random.nextInt(100) + 1;
        when(inventoryService.getInventoriesByWeapon(weaponId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/weapon/" + weaponId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenWeaponIDIsNotValid_getInventorysByWeapon_ReturnsEmptyList() throws Exception {
        int weaponId = random.nextInt(100) + 1;
        when(inventoryService.getInventoriesByWeapon(weaponId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/weapon/" + weaponId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenPlaceIDIsValid_getInventorysByPlace_ReturnsInventorys() throws Exception {
        int placeId = random.nextInt(100) + 1;
        when(inventoryService.getInventoriesByPlace(placeId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/place/" + placeId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenPlaceIDIsNotValid_getInventorysByPlace_ReturnsEmptyList() throws Exception {
        int placeId = random.nextInt(100) + 1;
        when(inventoryService.getInventoriesByPlace(placeId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/place/" + placeId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenInventoryIsValid_saveInventory_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        InventoryDTO requestDto = new InventoryDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(inventoryService, times(1)).saveInventory(any(InventoryDTO.class));
    }

    @Test
    void whenInventoryIsNotValid_saveInventory_RespondsBadRequest() throws Exception {
        InventoryDTO invalidInventory = new InventoryDTO();
        invalidInventory.setId(2);
        invalidInventory.setDescription("This is a description");
        invalidInventory.setFk_campaign_uuid(null); // Invalid UUID

        String requestJson = objectMapper.writeValueAsString(invalidInventory);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(inventoryService).saveInventory(any(InventoryDTO.class));

        MvcResult result = mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Inventory name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(inventoryService, times(0)).saveInventory(any(InventoryDTO.class));
    }

    @Test
    void whenInventoryIdIsValid_deleteInventory_RespondsOkRequest() throws Exception {
        when(inventoryService.getInventory(VALID_INVENTORY_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_INVENTORY_ID))
                .andExpect(status().isOk());

        verify(inventoryService, times(1)).deleteInventory(VALID_INVENTORY_ID);
    }

    @Test
    void whenInventoryIdIsInvalid_deleteInventory_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(inventoryService).deleteInventory(INVALID_INVENTORY_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_INVENTORY_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(inventoryService, times(1)).deleteInventory(INVALID_INVENTORY_ID);
    }

    @Test
    void whenInventoryIdIsValid_updateInventory_RespondsOkRequest() throws Exception {
        InventoryDTO updatedDto = new InventoryDTO();
        updatedDto.setId(VALID_INVENTORY_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_INVENTORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(inventoryService, times(1)).updateInventory(eq(VALID_INVENTORY_ID), any(InventoryDTO.class));
    }

    @Test
    void whenInventoryIdIsInvalid_updateInventory_RespondsBadRequest() throws Exception {
        InventoryDTO updatedDto = new InventoryDTO();
        updatedDto.setId(INVALID_INVENTORY_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(inventoryService).updateInventory(eq(INVALID_INVENTORY_ID), any(InventoryDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_INVENTORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenInventoryNameIsInvalid_updateInventory_RespondsBadRequest() throws Exception {
        InventoryDTO invalidDto = new InventoryDTO();
        invalidDto.setId(VALID_INVENTORY_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(inventoryService.updateInventory(eq(VALID_INVENTORY_ID), any(InventoryDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_INVENTORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
