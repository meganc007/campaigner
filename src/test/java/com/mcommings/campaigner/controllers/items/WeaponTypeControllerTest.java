package com.mcommings.campaigner.controllers.items;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.items.controllers.WeaponTypeController;
import com.mcommings.campaigner.modules.items.dtos.WeaponTypeDTO;
import com.mcommings.campaigner.modules.items.entities.WeaponType;
import com.mcommings.campaigner.modules.items.services.WeaponTypeService;
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

@WebMvcTest(WeaponTypeController.class)
public class WeaponTypeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    WeaponTypeService weaponTypeService;

    private static final int VALID_WEAPONTYPE_ID = 1;
    private static final int INVALID_WEAPONTYPE_ID = 999;
    private static final String URI = "/api/weapontypes";
    private WeaponType entity;
    private WeaponTypeDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new WeaponType();
        entity.setId(VALID_WEAPONTYPE_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");

        dto = new WeaponTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Test
    void whenThereAreWeaponTypes_getWeaponTypes_ReturnsWeaponTypes() throws Exception {
        when(weaponTypeService.getWeaponTypes()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreWeaponTypes_getWeaponTypes_ReturnsEmptyList() throws Exception {
        when(weaponTypeService.getWeaponTypes()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAWeaponType_getWeaponType_ReturnsWeaponType() throws Exception {
        when(weaponTypeService.getWeaponType(VALID_WEAPONTYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_WEAPONTYPE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_WEAPONTYPE_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAWeaponType_getWeaponType_ThrowsIllegalArgumentException() throws Exception {
        when(weaponTypeService.getWeaponType(INVALID_WEAPONTYPE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_WEAPONTYPE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getWeaponType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getWeaponType_ReturnsInternalServerError() throws Exception {
        when(weaponTypeService.getWeaponType(VALID_WEAPONTYPE_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_WEAPONTYPE_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getWeaponType_ReturnsInternalServerError() throws Exception {
        when(weaponTypeService.getWeaponType(VALID_WEAPONTYPE_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_WEAPONTYPE_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getWeaponType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenWeaponTypeIsValid_saveWeaponType_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        WeaponTypeDTO requestDto = new WeaponTypeDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/weapontypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(weaponTypeService, times(1)).saveWeaponType(any(WeaponTypeDTO.class));
    }

    @Test
    void whenWeaponTypeIsNotValid_saveWeaponType_RespondsBadRequest() throws Exception {
        WeaponTypeDTO invalidWeaponType = new WeaponTypeDTO();
        invalidWeaponType.setId(2);
        invalidWeaponType.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(invalidWeaponType);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(weaponTypeService).saveWeaponType(any(WeaponTypeDTO.class));

        MvcResult result = mockMvc.perform(post("/api/weapontypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("WeaponType name cannot be empty"));

        verify(weaponTypeService, times(0)).saveWeaponType(any(WeaponTypeDTO.class));
    }

    @Test
    void whenWeaponTypeIdIsValid_deleteWeaponType_RespondsOkRequest() throws Exception {
        when(weaponTypeService.getWeaponType(VALID_WEAPONTYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_WEAPONTYPE_ID))
                .andExpect(status().isOk());

        verify(weaponTypeService, times(1)).deleteWeaponType(VALID_WEAPONTYPE_ID);
    }

    @Test
    void whenWeaponTypeIdIsInvalid_deleteWeaponType_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(weaponTypeService).deleteWeaponType(INVALID_WEAPONTYPE_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_WEAPONTYPE_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(weaponTypeService, times(1)).deleteWeaponType(INVALID_WEAPONTYPE_ID);
    }

    @Test
    void whenWeaponTypeIdIsValid_updateWeaponType_RespondsOkRequest() throws Exception {
        WeaponTypeDTO updatedDto = new WeaponTypeDTO();
        updatedDto.setId(VALID_WEAPONTYPE_ID);
        updatedDto.setDescription("Updated description");

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_WEAPONTYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(weaponTypeService, times(1)).updateWeaponType(eq(VALID_WEAPONTYPE_ID), any(WeaponTypeDTO.class));
    }

    @Test
    void whenWeaponTypeIdIsInvalid_updateWeaponType_RespondsBadRequest() throws Exception {
        WeaponTypeDTO updatedDto = new WeaponTypeDTO();
        updatedDto.setId(INVALID_WEAPONTYPE_ID);
        updatedDto.setDescription("Some update");

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(weaponTypeService).updateWeaponType(eq(INVALID_WEAPONTYPE_ID), any(WeaponTypeDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_WEAPONTYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenWeaponTypeNameIsInvalid_updateWeaponType_RespondsBadRequest() throws Exception {
        WeaponTypeDTO invalidDto = new WeaponTypeDTO();
        invalidDto.setId(VALID_WEAPONTYPE_ID);
        invalidDto.setDescription("");

        String json = objectMapper.writeValueAsString(invalidDto);

        when(weaponTypeService.updateWeaponType(eq(VALID_WEAPONTYPE_ID), any(WeaponTypeDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_WEAPONTYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
