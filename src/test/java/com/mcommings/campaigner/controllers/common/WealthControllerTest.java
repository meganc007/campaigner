package com.mcommings.campaigner.controllers.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.common.controllers.WealthController;
import com.mcommings.campaigner.modules.common.dtos.WealthDTO;
import com.mcommings.campaigner.modules.common.entities.Wealth;
import com.mcommings.campaigner.modules.common.services.WealthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WealthController.class)
public class WealthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    WealthService wealthService;

    private static final int VALID_WEALTH_ID = 1;
    private static final int INVALID_WEALTH_ID = 999;
    private static final String URI = "/api/wealth";
    private Wealth entity;
    private WealthDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Wealth();
        entity.setId(VALID_WEALTH_ID);
        entity.setName("A name.");

        dto = new WealthDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
    }

    @Test
    void whenThereAreWealth_getWealth_ReturnsWealth() throws Exception {
        when(wealthService.getWealth()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreWealth_getWealth_ReturnsEmptyList() throws Exception {
        when(wealthService.getWealth()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenServiceFails_getWealth_ReturnsInternalServerError() throws Exception {
        when(wealthService.getWealth()).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenWealthIsValid_saveWealth_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        WealthDTO requestDto = new WealthDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/wealth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(wealthService, times(1)).saveWealth(any(WealthDTO.class));
    }

    @Test
    void whenWealthIsNotValid_saveWealth_RespondsBadRequest() throws Exception {
        WealthDTO invalidWealth = new WealthDTO();
        invalidWealth.setId(2);

        String requestJson = objectMapper.writeValueAsString(invalidWealth);

        MvcResult result = mockMvc.perform(post("/api/wealth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Wealth name cannot be empty"));

        verify(wealthService, times(0)).saveWealth(any(WealthDTO.class));
    }

    @Test
    void whenWealthIdIsValid_deleteWealth_RespondsOkRequest() throws Exception {
        when(wealthService.getWealth()).thenReturn(List.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_WEALTH_ID))
                .andExpect(status().isOk());

        verify(wealthService, times(1)).deleteWealth(VALID_WEALTH_ID);
    }

    @Test
    void whenWealthIdIsInvalid_deleteWealth_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This Wealth was not found."))
                .when(wealthService).deleteWealth(INVALID_WEALTH_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_WEALTH_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This Wealth was not found."));

        verify(wealthService, times(1)).deleteWealth(INVALID_WEALTH_ID);
    }

    @Test
    void whenWealthIdIsValid_updateWealth_RespondsOkRequest() throws Exception {
        WealthDTO updatedDto = new WealthDTO();
        updatedDto.setId(VALID_WEALTH_ID);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_WEALTH_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(wealthService, times(1)).updateWealth(eq(VALID_WEALTH_ID), any(WealthDTO.class));
    }

    @Test
    void whenWealthIdIsInvalid_updateWealth_RespondsBadRequest() throws Exception {
        WealthDTO updatedDto = new WealthDTO();
        updatedDto.setId(INVALID_WEALTH_ID);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This Wealth was not found."))
                .when(wealthService).updateWealth(eq(INVALID_WEALTH_ID), any(WealthDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_WEALTH_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This Wealth was not found.")));
    }

    @Test
    void whenWealthNameIsInvalid_updateWealth_RespondsBadRequest() throws Exception {
        WealthDTO invalidDto = new WealthDTO();
        invalidDto.setId(VALID_WEALTH_ID);

        String json = objectMapper.writeValueAsString(invalidDto);

        when(wealthService.updateWealth(eq(VALID_WEALTH_ID), any(WealthDTO.class)))
                .thenThrow(new IllegalArgumentException("Wealth name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_WEALTH_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Wealth name cannot be null or empty.")));
    }
}
