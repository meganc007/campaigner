package com.mcommings.campaigner.controllers.quests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.quests.controllers.HookController;
import com.mcommings.campaigner.modules.quests.dtos.HookDTO;
import com.mcommings.campaigner.modules.quests.entities.Hook;
import com.mcommings.campaigner.modules.quests.services.HookService;
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
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HookController.class)
public class HookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    HookService hookService;

    private static final int VALID_HOOK_ID = 1;
    private static final int INVALID_HOOK_ID = 999;
    private static final String URI = "/api/hooks";
    private Hook entity;
    private HookDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Hook();
        entity.setId(1);
        entity.setDescription("This is a description.");
        entity.setNotes("This is a note.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new HookDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
    }

    @Test
    void whenThereAreHooks_getHooks_ReturnsHooks() throws Exception {
        when(hookService.getHooks()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreHooks_getHooks_ReturnsEmptyList() throws Exception {
        when(hookService.getHooks()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAHook_getHook_ReturnsHook() throws Exception {
        when(hookService.getHook(VALID_HOOK_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_HOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_HOOK_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAHook_getHook_ThrowsIllegalArgumentException() throws Exception {
        when(hookService.getHook(INVALID_HOOK_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_HOOK_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getHook_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getHook_ReturnsInternalServerError() throws Exception {
        when(hookService.getHook(VALID_HOOK_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_HOOK_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getHook_ReturnsInternalServerError() throws Exception {
        when(hookService.getHook(VALID_HOOK_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_HOOK_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getHooksByCampaignUUID_ReturnsHooks() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(hookService.getHooksByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getHooksByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(hookService.getHooksByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getHook_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenKeywordIsValid_getHooksWhereDescriptionContainsKeyword_ReturnsHooks() throws Exception {
        UUID uuid = UUID.randomUUID();
        String keyword = "magic sword";

        HookDTO hook1 = new HookDTO();
        hook1.setId(1);
        hook1.setDescription("A villager found a magic sword.");
        hook1.setFk_campaign_uuid(uuid);

        HookDTO hook2 = new HookDTO();
        hook2.setId(2);
        hook2.setDescription("A noble wants the party to retrieve their magic sword.");
        hook2.setFk_campaign_uuid(uuid);

        HookDTO hook3 = new HookDTO();
        hook3.setId(2);
        hook3.setDescription("You come across a winged cat hunting carrier pigeons.");
        hook3.setFk_campaign_uuid(uuid);

        List<HookDTO> hooks = List.of(hook1, hook2);

        when(hookService.getHooksWhereDescriptionContainsKeyword(keyword)).thenReturn(hooks);

        MvcResult result = mockMvc.perform(get(URI + "/description/" + keyword))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<HookDTO> returnedHooks = objectMapper.readValue(responseJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, HookDTO.class));

        assertTrue(returnedHooks.contains(hook1));
        assertTrue(returnedHooks.contains(hook2));
        assertFalse(returnedHooks.contains(hook3));
    }

    @Test
    void whenKeywordIsValidButThereAreNoMatches_getHooksWhereDescriptionContainsKeyword_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        String keyword = "dog";

        HookDTO hook1 = new HookDTO();
        hook1.setId(1);
        hook1.setDescription("A villager found a magic sword.");
        hook1.setFk_campaign_uuid(uuid);

        HookDTO hook2 = new HookDTO();
        hook2.setId(2);
        hook2.setDescription("A noble wants the party to retrieve their magic sword.");
        hook2.setFk_campaign_uuid(uuid);

        HookDTO hook3 = new HookDTO();
        hook3.setId(2);
        hook3.setDescription("You come across a winged cat hunting carrier pigeons.");
        hook3.setFk_campaign_uuid(uuid);

        when(hookService.getHooksWhereDescriptionContainsKeyword(keyword)).thenReturn(List.of());

        MvcResult result = mockMvc.perform(get(URI + "/description/" + keyword))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<HookDTO> returnedHooks = objectMapper.readValue(responseJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, HookDTO.class));

        assertFalse(returnedHooks.contains(hook1));
        assertFalse(returnedHooks.contains(hook2));
        assertFalse(returnedHooks.contains(hook3));
    }

    @Test
    void whenHookIsValid_saveHook_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        HookDTO requestDto = new HookDTO();
        requestDto.setId(2);
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/hooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(hookService, times(1)).saveHook(any(HookDTO.class));
    }

    @Test
    void whenHookIsNotValid_saveHook_RespondsBadRequest() throws Exception {
        HookDTO invalidHook = new HookDTO();
        invalidHook.setId(2);
        invalidHook.setDescription("");
        invalidHook.setFk_campaign_uuid(null); // Invalid UUID

        String requestJson = objectMapper.writeValueAsString(invalidHook);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(hookService).saveHook(any(HookDTO.class));

        MvcResult result = mockMvc.perform(post("/api/hooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Hook description cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(hookService, times(0)).saveHook(any(HookDTO.class));
    }

    @Test
    void whenHookIdIsValid_deleteHook_RespondsOkRequest() throws Exception {
        when(hookService.getHook(VALID_HOOK_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_HOOK_ID))
                .andExpect(status().isOk());

        verify(hookService, times(1)).deleteHook(VALID_HOOK_ID);
    }

    @Test
    void whenHookIdIsInvalid_deleteHook_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(hookService).deleteHook(INVALID_HOOK_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_HOOK_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(hookService, times(1)).deleteHook(INVALID_HOOK_ID);
    }

    @Test
    void whenHookIdIsValid_updateHook_RespondsOkRequest() throws Exception {
        HookDTO updatedDto = new HookDTO();
        updatedDto.setId(VALID_HOOK_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_HOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(hookService, times(1)).updateHook(eq(VALID_HOOK_ID), any(HookDTO.class));
    }

    @Test
    void whenHookIdIsInvalid_updateHook_RespondsBadRequest() throws Exception {
        HookDTO updatedDto = new HookDTO();
        updatedDto.setId(INVALID_HOOK_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(hookService).updateHook(eq(INVALID_HOOK_ID), any(HookDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_HOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenHookDescriptionIsInvalid_updateHook_RespondsBadRequest() throws Exception {
        HookDTO invalidDto = new HookDTO();
        invalidDto.setId(VALID_HOOK_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(hookService.updateHook(eq(VALID_HOOK_ID), any(HookDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_HOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }

}
