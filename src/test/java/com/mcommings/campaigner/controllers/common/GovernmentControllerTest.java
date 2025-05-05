package com.mcommings.campaigner.controllers.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.common.controllers.GovernmentController;
import com.mcommings.campaigner.modules.common.dtos.GovernmentDTO;
import com.mcommings.campaigner.modules.common.entities.Government;
import com.mcommings.campaigner.modules.common.services.GovernmentService;
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

@WebMvcTest(GovernmentController.class)
public class GovernmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    GovernmentService governmentService;

    private static final int VALID_GOVERNMENT_ID = 1;
    private static final int INVALID_GOVERNMENT_ID = 999;
    private static final String URI = "/api/governments";
    private Government entity;
    private GovernmentDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Government();
        entity.setId(VALID_GOVERNMENT_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");

        dto = new GovernmentDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Test
    void whenThereAreGovernments_getGovernments_ReturnsGovernments() throws Exception {
        when(governmentService.getGovernments()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreGovernments_getGovernments_ReturnsEmptyList() throws Exception {
        when(governmentService.getGovernments()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAGovernment_getGovernment_ReturnsGovernment() throws Exception {
        when(governmentService.getGovernment(VALID_GOVERNMENT_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_GOVERNMENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_GOVERNMENT_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAGovernment_getGovernment_ThrowsIllegalArgumentException() throws Exception {
        when(governmentService.getGovernment(INVALID_GOVERNMENT_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_GOVERNMENT_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getGovernment_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getGovernment_ReturnsInternalServerError() throws Exception {
        when(governmentService.getGovernment(VALID_GOVERNMENT_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_GOVERNMENT_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getGovernment_ReturnsInternalServerError() throws Exception {
        when(governmentService.getGovernment(VALID_GOVERNMENT_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_GOVERNMENT_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenGovernmentIsValid_saveGovernment_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        GovernmentDTO requestDto = new GovernmentDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/governments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(governmentService, times(1)).saveGovernment(any(GovernmentDTO.class));
    }

    @Test
    void whenGovernmentIsNotValid_saveGovernment_RespondsBadRequest() throws Exception {
        GovernmentDTO invalidGovernment = new GovernmentDTO();
        invalidGovernment.setId(2);
        invalidGovernment.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(invalidGovernment);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(governmentService).saveGovernment(any(GovernmentDTO.class));

        MvcResult result = mockMvc.perform(post("/api/governments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Government name cannot be empty"));

        verify(governmentService, times(0)).saveGovernment(any(GovernmentDTO.class));
    }

    @Test
    void whenGovernmentIdIsValid_deleteGovernment_RespondsOkRequest() throws Exception {
        when(governmentService.getGovernment(VALID_GOVERNMENT_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_GOVERNMENT_ID))
                .andExpect(status().isOk());

        verify(governmentService, times(1)).deleteGovernment(VALID_GOVERNMENT_ID);
    }

    @Test
    void whenGovernmentIdIsInvalid_deleteGovernment_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(governmentService).deleteGovernment(INVALID_GOVERNMENT_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_GOVERNMENT_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(governmentService, times(1)).deleteGovernment(INVALID_GOVERNMENT_ID);
    }

    @Test
    void whenGovernmentIdIsValid_updateGovernment_RespondsOkRequest() throws Exception {
        GovernmentDTO updatedDto = new GovernmentDTO();
        updatedDto.setId(VALID_GOVERNMENT_ID);
        updatedDto.setDescription("Updated description");

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_GOVERNMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(governmentService, times(1)).updateGovernment(eq(VALID_GOVERNMENT_ID), any(GovernmentDTO.class));
    }

    @Test
    void whenGovernmentIdIsInvalid_updateGovernment_RespondsBadRequest() throws Exception {
        GovernmentDTO updatedDto = new GovernmentDTO();
        updatedDto.setId(INVALID_GOVERNMENT_ID);
        updatedDto.setDescription("Some update");

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(governmentService).updateGovernment(eq(INVALID_GOVERNMENT_ID), any(GovernmentDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_GOVERNMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenGovernmentNameIsInvalid_updateGovernment_RespondsBadRequest() throws Exception {
        GovernmentDTO invalidDto = new GovernmentDTO();
        invalidDto.setId(VALID_GOVERNMENT_ID);
        invalidDto.setDescription("");

        String json = objectMapper.writeValueAsString(invalidDto);

        when(governmentService.updateGovernment(eq(VALID_GOVERNMENT_ID), any(GovernmentDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_GOVERNMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
