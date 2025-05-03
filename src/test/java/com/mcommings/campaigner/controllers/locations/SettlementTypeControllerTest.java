package com.mcommings.campaigner.controllers.locations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.locations.controllers.SettlementTypeController;
import com.mcommings.campaigner.modules.locations.dtos.SettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.SettlementType;
import com.mcommings.campaigner.modules.locations.services.SettlementTypeService;
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

@WebMvcTest(SettlementTypeController.class)
public class SettlementTypeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    SettlementTypeService settlementTypeService;

    private static final int VALID_SETTLEMENTTYPE_ID = 1;
    private static final int INVALID_SETTLEMENTTYPE_ID = 999;
    private static final String URI = "/api/settlementtypes";
    private SettlementType entity;
    private SettlementTypeDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new SettlementType();
        entity.setId(VALID_SETTLEMENTTYPE_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");

        dto = new SettlementTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Test
    void whenThereAreSettlementTypes_getSettlementTypes_ReturnsSettlementTypes() throws Exception {
        when(settlementTypeService.getSettlementTypes()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreSettlementTypes_getSettlementTypes_ReturnsEmptyList() throws Exception {
        when(settlementTypeService.getSettlementTypes()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsASettlementType_getSettlementType_ReturnsSettlementType() throws Exception {
        when(settlementTypeService.getSettlementType(VALID_SETTLEMENTTYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_SETTLEMENTTYPE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_SETTLEMENTTYPE_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotASettlementType_getSettlementType_ThrowsIllegalArgumentException() throws Exception {
        when(settlementTypeService.getSettlementType(INVALID_SETTLEMENTTYPE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_SETTLEMENTTYPE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getSettlementType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getSettlementType_ReturnsInternalServerError() throws Exception {
        when(settlementTypeService.getSettlementType(VALID_SETTLEMENTTYPE_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_SETTLEMENTTYPE_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getSettlementType_ReturnsInternalServerError() throws Exception {
        when(settlementTypeService.getSettlementType(VALID_SETTLEMENTTYPE_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_SETTLEMENTTYPE_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getSettlementType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenSettlementTypeIsValid_saveSettlementType_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        SettlementTypeDTO requestDto = new SettlementTypeDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/settlementtypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(settlementTypeService, times(1)).saveSettlementType(any(SettlementTypeDTO.class));
    }

    @Test
    void whenSettlementTypeIsNotValid_saveSettlementType_RespondsBadRequest() throws Exception {
        SettlementTypeDTO invalidSettlementType = new SettlementTypeDTO();
        invalidSettlementType.setId(2);
        invalidSettlementType.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(invalidSettlementType);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(settlementTypeService).saveSettlementType(any(SettlementTypeDTO.class));

        MvcResult result = mockMvc.perform(post("/api/settlementtypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("SettlementType name cannot be empty"));

        verify(settlementTypeService, times(0)).saveSettlementType(any(SettlementTypeDTO.class));
    }

    @Test
    void whenSettlementTypeIdIsValid_deleteSettlementType_RespondsOkRequest() throws Exception {
        when(settlementTypeService.getSettlementType(VALID_SETTLEMENTTYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_SETTLEMENTTYPE_ID))
                .andExpect(status().isOk());

        verify(settlementTypeService, times(1)).deleteSettlementType(VALID_SETTLEMENTTYPE_ID);
    }

    @Test
    void whenSettlementTypeIdIsInvalid_deleteSettlementType_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(settlementTypeService).deleteSettlementType(INVALID_SETTLEMENTTYPE_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_SETTLEMENTTYPE_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(settlementTypeService, times(1)).deleteSettlementType(INVALID_SETTLEMENTTYPE_ID);
    }

    @Test
    void whenSettlementTypeIdIsValid_updateSettlementType_RespondsOkRequest() throws Exception {
        SettlementTypeDTO updatedDto = new SettlementTypeDTO();
        updatedDto.setId(VALID_SETTLEMENTTYPE_ID);
        updatedDto.setDescription("Updated description");

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_SETTLEMENTTYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(settlementTypeService, times(1)).updateSettlementType(eq(VALID_SETTLEMENTTYPE_ID), any(SettlementTypeDTO.class));
    }

    @Test
    void whenSettlementTypeIdIsInvalid_updateSettlementType_RespondsBadRequest() throws Exception {
        SettlementTypeDTO updatedDto = new SettlementTypeDTO();
        updatedDto.setId(INVALID_SETTLEMENTTYPE_ID);
        updatedDto.setDescription("Some update");

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(settlementTypeService).updateSettlementType(eq(INVALID_SETTLEMENTTYPE_ID), any(SettlementTypeDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_SETTLEMENTTYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenSettlementTypeNameIsInvalid_updateSettlementType_RespondsBadRequest() throws Exception {
        SettlementTypeDTO invalidDto = new SettlementTypeDTO();
        invalidDto.setId(VALID_SETTLEMENTTYPE_ID);
        invalidDto.setDescription("");

        String json = objectMapper.writeValueAsString(invalidDto);

        when(settlementTypeService.updateSettlementType(eq(VALID_SETTLEMENTTYPE_ID), any(SettlementTypeDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_SETTLEMENTTYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
