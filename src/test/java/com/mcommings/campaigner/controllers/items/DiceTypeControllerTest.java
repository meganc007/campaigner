package com.mcommings.campaigner.controllers.items;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.items.controllers.DiceTypeController;
import com.mcommings.campaigner.modules.items.dtos.DiceTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DiceType;
import com.mcommings.campaigner.modules.items.services.DiceTypeService;
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

@WebMvcTest(DiceTypeController.class)
public class DiceTypeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    DiceTypeService diceTypeService;

    private static final int VALID_DICETYPE_ID = 1;
    private static final int INVALID_DICETYPE_ID = 999;
    private static final String URI = "/api/dicetypes";
    private DiceType entity;
    private DiceTypeDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new DiceType();
        entity.setId(VALID_DICETYPE_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setMax_roll(random.nextInt(100) + 1);

        dto = new DiceTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setMax_roll(entity.getMax_roll());
    }

    @Test
    void whenThereAreDiceTypes_getDiceTypes_ReturnsDiceTypes() throws Exception {
        when(diceTypeService.getDiceTypes()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreDiceTypes_getDiceTypes_ReturnsEmptyList() throws Exception {
        when(diceTypeService.getDiceTypes()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsADiceType_getDiceType_ReturnsDiceType() throws Exception {
        when(diceTypeService.getDiceType(VALID_DICETYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_DICETYPE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_DICETYPE_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotADiceType_getDiceType_ThrowsIllegalArgumentException() throws Exception {
        when(diceTypeService.getDiceType(INVALID_DICETYPE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_DICETYPE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getDiceType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getDiceType_ReturnsInternalServerError() throws Exception {
        when(diceTypeService.getDiceType(VALID_DICETYPE_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_DICETYPE_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getDiceType_ReturnsInternalServerError() throws Exception {
        when(diceTypeService.getDiceType(VALID_DICETYPE_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_DICETYPE_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getDiceType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDiceTypeIsValid_saveDiceType_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        DiceTypeDTO requestDto = new DiceTypeDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/dicetypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(diceTypeService, times(1)).saveDiceType(any(DiceTypeDTO.class));
    }

    @Test
    void whenDiceTypeIsNotValid_saveDiceType_RespondsBadRequest() throws Exception {
        DiceTypeDTO invalidDiceType = new DiceTypeDTO();
        invalidDiceType.setId(2);
        invalidDiceType.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(invalidDiceType);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(diceTypeService).saveDiceType(any(DiceTypeDTO.class));

        MvcResult result = mockMvc.perform(post("/api/dicetypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("DiceType name cannot be empty"));

        verify(diceTypeService, times(0)).saveDiceType(any(DiceTypeDTO.class));
    }

    @Test
    void whenDiceTypeIdIsValid_deleteDiceType_RespondsOkRequest() throws Exception {
        when(diceTypeService.getDiceType(VALID_DICETYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_DICETYPE_ID))
                .andExpect(status().isOk());

        verify(diceTypeService, times(1)).deleteDiceType(VALID_DICETYPE_ID);
    }

    @Test
    void whenDiceTypeIdIsInvalid_deleteDiceType_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(diceTypeService).deleteDiceType(INVALID_DICETYPE_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_DICETYPE_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(diceTypeService, times(1)).deleteDiceType(INVALID_DICETYPE_ID);
    }

    @Test
    void whenDiceTypeIdIsValid_updateDiceType_RespondsOkRequest() throws Exception {
        DiceTypeDTO updatedDto = new DiceTypeDTO();
        updatedDto.setId(VALID_DICETYPE_ID);
        updatedDto.setDescription("Updated description");

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_DICETYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(diceTypeService, times(1)).updateDiceType(eq(VALID_DICETYPE_ID), any(DiceTypeDTO.class));
    }

    @Test
    void whenDiceTypeIdIsInvalid_updateDiceType_RespondsBadRequest() throws Exception {
        DiceTypeDTO updatedDto = new DiceTypeDTO();
        updatedDto.setId(INVALID_DICETYPE_ID);
        updatedDto.setDescription("Some update");

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(diceTypeService).updateDiceType(eq(INVALID_DICETYPE_ID), any(DiceTypeDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_DICETYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenDiceTypeNameIsInvalid_updateDiceType_RespondsBadRequest() throws Exception {
        DiceTypeDTO invalidDto = new DiceTypeDTO();
        invalidDto.setId(VALID_DICETYPE_ID);
        invalidDto.setDescription("");

        String json = objectMapper.writeValueAsString(invalidDto);

        when(diceTypeService.updateDiceType(eq(VALID_DICETYPE_ID), any(DiceTypeDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_DICETYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
