package com.mcommings.campaigner.controllers.items;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.items.controllers.DamageTypeController;
import com.mcommings.campaigner.modules.items.dtos.DamageTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DamageType;
import com.mcommings.campaigner.modules.items.services.DamageTypeService;
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

@WebMvcTest(DamageTypeController.class)
class DamageTypeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    DamageTypeService damageTypeService;

    private static final int VALID_DAMAGETYPE_ID = 1;
    private static final int INVALID_DAMAGETYPE_ID = 999;
    private static final String URI = "/api/damagetypes";
    private DamageType entity;
    private DamageTypeDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new DamageType();
        entity.setId(VALID_DAMAGETYPE_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");

        dto = new DamageTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Test
    void whenThereAreDamageTypes_getDamageTypes_ReturnsDamageTypes() throws Exception {
        when(damageTypeService.getDamageTypes()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreDamageTypes_getDamageTypes_ReturnsEmptyList() throws Exception {
        when(damageTypeService.getDamageTypes()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsADamageType_getDamageType_ReturnsDamageType() throws Exception {
        when(damageTypeService.getDamageType(VALID_DAMAGETYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_DAMAGETYPE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_DAMAGETYPE_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotADamageType_getDamageType_ThrowsIllegalArgumentException() throws Exception {
        when(damageTypeService.getDamageType(INVALID_DAMAGETYPE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_DAMAGETYPE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getDamageType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getDamageType_ReturnsInternalServerError() throws Exception {
        when(damageTypeService.getDamageType(VALID_DAMAGETYPE_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_DAMAGETYPE_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getDamageType_ReturnsInternalServerError() throws Exception {
        when(damageTypeService.getDamageType(VALID_DAMAGETYPE_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_DAMAGETYPE_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getDamageType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDamageTypeIsValid_saveDamageType_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        DamageTypeDTO requestDto = new DamageTypeDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/damagetypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(damageTypeService, times(1)).saveDamageType(any(DamageTypeDTO.class));
    }

    @Test
    void whenDamageTypeIsNotValid_saveDamageType_RespondsBadRequest() throws Exception {
        DamageTypeDTO invalidDamageType = new DamageTypeDTO();
        invalidDamageType.setId(2);
        invalidDamageType.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(invalidDamageType);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(damageTypeService).saveDamageType(any(DamageTypeDTO.class));

        MvcResult result = mockMvc.perform(post("/api/damagetypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("DamageType name cannot be empty"));

        verify(damageTypeService, times(0)).saveDamageType(any(DamageTypeDTO.class));
    }

    @Test
    void whenDamageTypeIdIsValid_deleteDamageType_RespondsOkRequest() throws Exception {
        when(damageTypeService.getDamageType(VALID_DAMAGETYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_DAMAGETYPE_ID))
                .andExpect(status().isOk());

        verify(damageTypeService, times(1)).deleteDamageType(VALID_DAMAGETYPE_ID);
    }

    @Test
    void whenDamageTypeIdIsInvalid_deleteDamageType_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(damageTypeService).deleteDamageType(INVALID_DAMAGETYPE_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_DAMAGETYPE_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(damageTypeService, times(1)).deleteDamageType(INVALID_DAMAGETYPE_ID);
    }

    @Test
    void whenDamageTypeIdIsValid_updateDamageType_RespondsOkRequest() throws Exception {
        DamageTypeDTO updatedDto = new DamageTypeDTO();
        updatedDto.setId(VALID_DAMAGETYPE_ID);
        updatedDto.setDescription("Updated description");

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_DAMAGETYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(damageTypeService, times(1)).updateDamageType(eq(VALID_DAMAGETYPE_ID), any(DamageTypeDTO.class));
    }

    @Test
    void whenDamageTypeIdIsInvalid_updateDamageType_RespondsBadRequest() throws Exception {
        DamageTypeDTO updatedDto = new DamageTypeDTO();
        updatedDto.setId(INVALID_DAMAGETYPE_ID);
        updatedDto.setDescription("Some update");

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(damageTypeService).updateDamageType(eq(INVALID_DAMAGETYPE_ID), any(DamageTypeDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_DAMAGETYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenDamageTypeNameIsInvalid_updateDamageType_RespondsBadRequest() throws Exception {
        DamageTypeDTO invalidDto = new DamageTypeDTO();
        invalidDto.setId(VALID_DAMAGETYPE_ID);
        invalidDto.setDescription("");

        String json = objectMapper.writeValueAsString(invalidDto);

        when(damageTypeService.updateDamageType(eq(VALID_DAMAGETYPE_ID), any(DamageTypeDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_DAMAGETYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
