package com.mcommings.campaigner.controllers.items;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.items.controllers.ItemTypeController;
import com.mcommings.campaigner.modules.items.dtos.ItemTypeDTO;
import com.mcommings.campaigner.modules.items.entities.ItemType;
import com.mcommings.campaigner.modules.items.services.ItemTypeService;
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

@WebMvcTest(ItemTypeController.class)
public class ItemTypeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    ItemTypeService itemTypeService;

    private static final int VALID_ITEMTYPE_ID = 1;
    private static final int INVALID_ITEMTYPE_ID = 999;
    private static final String URI = "/api/itemtypes";
    private ItemType entity;
    private ItemTypeDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new ItemType();
        entity.setId(VALID_ITEMTYPE_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");

        dto = new ItemTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Test
    void whenThereAreItemTypes_getItemTypes_ReturnsItemTypes() throws Exception {
        when(itemTypeService.getItemTypes()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreItemTypes_getItemTypes_ReturnsEmptyList() throws Exception {
        when(itemTypeService.getItemTypes()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAItemType_getItemType_ReturnsItemType() throws Exception {
        when(itemTypeService.getItemType(VALID_ITEMTYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_ITEMTYPE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_ITEMTYPE_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAItemType_getItemType_ThrowsIllegalArgumentException() throws Exception {
        when(itemTypeService.getItemType(INVALID_ITEMTYPE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_ITEMTYPE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getItemType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getItemType_ReturnsInternalServerError() throws Exception {
        when(itemTypeService.getItemType(VALID_ITEMTYPE_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_ITEMTYPE_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getItemType_ReturnsInternalServerError() throws Exception {
        when(itemTypeService.getItemType(VALID_ITEMTYPE_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_ITEMTYPE_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getItemType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenItemTypeIsValid_saveItemType_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        ItemTypeDTO requestDto = new ItemTypeDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/itemtypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(itemTypeService, times(1)).saveItemType(any(ItemTypeDTO.class));
    }

    @Test
    void whenItemTypeIsNotValid_saveItemType_RespondsBadRequest() throws Exception {
        ItemTypeDTO invalidItemType = new ItemTypeDTO();
        invalidItemType.setId(2);
        invalidItemType.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(invalidItemType);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(itemTypeService).saveItemType(any(ItemTypeDTO.class));

        MvcResult result = mockMvc.perform(post("/api/itemtypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("ItemType name cannot be empty"));

        verify(itemTypeService, times(0)).saveItemType(any(ItemTypeDTO.class));
    }

    @Test
    void whenItemTypeIdIsValid_deleteItemType_RespondsOkRequest() throws Exception {
        when(itemTypeService.getItemType(VALID_ITEMTYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_ITEMTYPE_ID))
                .andExpect(status().isOk());

        verify(itemTypeService, times(1)).deleteItemType(VALID_ITEMTYPE_ID);
    }

    @Test
    void whenItemTypeIdIsInvalid_deleteItemType_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(itemTypeService).deleteItemType(INVALID_ITEMTYPE_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_ITEMTYPE_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(itemTypeService, times(1)).deleteItemType(INVALID_ITEMTYPE_ID);
    }

    @Test
    void whenItemTypeIdIsValid_updateItemType_RespondsOkRequest() throws Exception {
        ItemTypeDTO updatedDto = new ItemTypeDTO();
        updatedDto.setId(VALID_ITEMTYPE_ID);
        updatedDto.setDescription("Updated description");

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_ITEMTYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(itemTypeService, times(1)).updateItemType(eq(VALID_ITEMTYPE_ID), any(ItemTypeDTO.class));
    }

    @Test
    void whenItemTypeIdIsInvalid_updateItemType_RespondsBadRequest() throws Exception {
        ItemTypeDTO updatedDto = new ItemTypeDTO();
        updatedDto.setId(INVALID_ITEMTYPE_ID);
        updatedDto.setDescription("Some update");

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(itemTypeService).updateItemType(eq(INVALID_ITEMTYPE_ID), any(ItemTypeDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_ITEMTYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenItemTypeNameIsInvalid_updateItemType_RespondsBadRequest() throws Exception {
        ItemTypeDTO invalidDto = new ItemTypeDTO();
        invalidDto.setId(VALID_ITEMTYPE_ID);
        invalidDto.setDescription("");

        String json = objectMapper.writeValueAsString(invalidDto);

        when(itemTypeService.updateItemType(eq(VALID_ITEMTYPE_ID), any(ItemTypeDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_ITEMTYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
