package com.mcommings.campaigner.controllers.items;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.items.controllers.ItemController;
import com.mcommings.campaigner.modules.items.dtos.ItemDTO;
import com.mcommings.campaigner.modules.items.entities.Item;
import com.mcommings.campaigner.modules.items.services.ItemService;
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

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    ItemService itemService;

    private static final int VALID_ITEM_ID = 1;
    private static final int INVALID_ITEM_ID = 999;
    private static final String URI = "/api/items";
    private Item entity;
    private ItemDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Item();
        entity.setId(VALID_ITEM_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_item_type(random.nextInt(100) + 1);
        entity.setRarity("Common.");
        entity.setGold_value(random.nextInt(100) + 1);
        entity.setSilver_value(random.nextInt(100) + 1);
        entity.setCopper_value(random.nextInt(100) + 1);
        entity.setWeight(random.nextFloat(100) + 1);
        entity.setIsMagical(true);
        entity.setIsCursed(false);
        entity.setNotes("Notes.");

        dto = new ItemDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_item_type(entity.getFk_item_type());
        dto.setGold_value(entity.getGold_value());
        dto.setSilver_value(entity.getSilver_value());
        dto.setCopper_value(entity.getCopper_value());
        dto.setWeight(entity.getWeight());
        dto.setIsMagical(entity.getIsMagical());
        dto.setIsCursed(entity.getIsCursed());
        dto.setNotes(entity.getNotes());
    }

    @Test
    void whenThereAreItems_getItems_ReturnsItems() throws Exception {
        when(itemService.getItems()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreItems_getItems_ReturnsEmptyList() throws Exception {
        when(itemService.getItems()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAItem_getItem_ReturnsItem() throws Exception {
        when(itemService.getItem(VALID_ITEM_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_ITEM_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_ITEM_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAItem_getItem_ThrowsIllegalArgumentException() throws Exception {
        when(itemService.getItem(INVALID_ITEM_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_ITEM_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getItem_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getItem_ReturnsInternalServerError() throws Exception {
        when(itemService.getItem(VALID_ITEM_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_ITEM_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getItem_ReturnsInternalServerError() throws Exception {
        when(itemService.getItem(VALID_ITEM_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_ITEM_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getItemsByCampaignUUID_ReturnsItems() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(itemService.getItemsByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getItemsByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(itemService.getItemsByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getItem_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenItemTypeIDIsValid_getItemsByItemType_ReturnsItems() throws Exception {
        int itemTypeId = random.nextInt(100) + 1;
        when(itemService.getItemsByItemType(itemTypeId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/itemtype/" + itemTypeId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenItemTypeIDIsNotValid_getItemsByItemType_ReturnsEmptyList() throws Exception {
        int itemTypeId = random.nextInt(100) + 1;
        when(itemService.getItemsByItemType(itemTypeId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/itemtype/" + itemTypeId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenItemIsValid_saveItem_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        ItemDTO requestDto = new ItemDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_item_type(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(itemService, times(1)).saveItem(any(ItemDTO.class));
    }

    @Test
    void whenItemIsNotValid_saveItem_RespondsBadRequest() throws Exception {
        ItemDTO invalidItem = new ItemDTO();
        invalidItem.setId(2);
        invalidItem.setDescription("This is a description");
        invalidItem.setFk_campaign_uuid(null); // Invalid UUID
        invalidItem.setFk_item_type(4);

        String requestJson = objectMapper.writeValueAsString(invalidItem);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(itemService).saveItem(any(ItemDTO.class));

        MvcResult result = mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Item name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(itemService, times(0)).saveItem(any(ItemDTO.class));
    }

    @Test
    void whenItemIdIsValid_deleteItem_RespondsOkRequest() throws Exception {
        when(itemService.getItem(VALID_ITEM_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_ITEM_ID))
                .andExpect(status().isOk());

        verify(itemService, times(1)).deleteItem(VALID_ITEM_ID);
    }

    @Test
    void whenItemIdIsInvalid_deleteItem_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(itemService).deleteItem(INVALID_ITEM_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_ITEM_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(itemService, times(1)).deleteItem(INVALID_ITEM_ID);
    }

    @Test
    void whenItemIdIsValid_updateItem_RespondsOkRequest() throws Exception {
        ItemDTO updatedDto = new ItemDTO();
        updatedDto.setId(VALID_ITEM_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_item_type(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_ITEM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(itemService, times(1)).updateItem(eq(VALID_ITEM_ID), any(ItemDTO.class));
    }

    @Test
    void whenItemIdIsInvalid_updateItem_RespondsBadRequest() throws Exception {
        ItemDTO updatedDto = new ItemDTO();
        updatedDto.setId(INVALID_ITEM_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_item_type(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(itemService).updateItem(eq(INVALID_ITEM_ID), any(ItemDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_ITEM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenItemNameIsInvalid_updateItem_RespondsBadRequest() throws Exception {
        ItemDTO invalidDto = new ItemDTO();
        invalidDto.setId(VALID_ITEM_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());
        invalidDto.setFk_item_type(1);

        String json = objectMapper.writeValueAsString(invalidDto);

        when(itemService.updateItem(eq(VALID_ITEM_ID), any(ItemDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_ITEM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
