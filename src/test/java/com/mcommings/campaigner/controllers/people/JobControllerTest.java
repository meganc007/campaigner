package com.mcommings.campaigner.controllers.people;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.people.controllers.JobController;
import com.mcommings.campaigner.modules.people.dtos.JobDTO;
import com.mcommings.campaigner.modules.people.entities.Job;
import com.mcommings.campaigner.modules.people.services.JobService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobController.class)
public class JobControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    JobService jobService;

    private static final int VALID_JOB_ID = 1;
    private static final int INVALID_JOB_ID = 999;
    private static final String URI = "/api/jobs";
    private Job entity;
    private JobDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Job();
        entity.setId(VALID_JOB_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");

        dto = new JobDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Test
    void whenThereAreJobs_getJobs_ReturnsJobs() throws Exception {
        when(jobService.getJobs()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreJobs_getJobs_ReturnsEmptyList() throws Exception {
        when(jobService.getJobs()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAJob_getJob_ReturnsJob() throws Exception {
        when(jobService.getJob(VALID_JOB_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_JOB_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_JOB_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAJob_getJob_ThrowsIllegalArgumentException() throws Exception {
        when(jobService.getJob(INVALID_JOB_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_JOB_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getJob_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getJob_ReturnsInternalServerError() throws Exception {
        when(jobService.getJob(VALID_JOB_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_JOB_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getJob_ReturnsInternalServerError() throws Exception {
        when(jobService.getJob(VALID_JOB_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_JOB_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenJobIsValid_saveJob_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        JobDTO requestDto = new JobDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(jobService, times(1)).saveJob(any(JobDTO.class));
    }

    @Test
    void whenJobIsNotValid_saveJob_RespondsBadRequest() throws Exception {
        JobDTO invalidJob = new JobDTO();
        invalidJob.setId(2);
        invalidJob.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(invalidJob);

        doThrow(new IllegalArgumentException("Job name cannot be empty"))
                .when(jobService).saveJob(any(JobDTO.class));

        MvcResult result = mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Job name cannot be empty"));

        verify(jobService, times(0)).saveJob(any(JobDTO.class));
    }

    @Test
    void whenJobIdIsValid_deleteJob_RespondsOkRequest() throws Exception {
        when(jobService.getJob(VALID_JOB_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_JOB_ID))
                .andExpect(status().isOk());

        verify(jobService, times(1)).deleteJob(VALID_JOB_ID);
    }

    @Test
    void whenJobIdIsInvalid_deleteJob_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(jobService).deleteJob(INVALID_JOB_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_JOB_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(jobService, times(1)).deleteJob(INVALID_JOB_ID);
    }

    @Test
    void whenJobIdIsValid_updateJob_RespondsOkRequest() throws Exception {
        JobDTO updatedDto = new JobDTO();
        updatedDto.setId(VALID_JOB_ID);
        updatedDto.setDescription("Updated description");

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_JOB_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(jobService, times(1)).updateJob(eq(VALID_JOB_ID), any(JobDTO.class));
    }

    @Test
    void whenJobIdIsInvalid_updateJob_RespondsBadRequest() throws Exception {
        JobDTO updatedDto = new JobDTO();
        updatedDto.setId(INVALID_JOB_ID);
        updatedDto.setDescription("Some update");

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(jobService).updateJob(eq(INVALID_JOB_ID), any(JobDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_JOB_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenJobNameIsInvalid_updateJob_RespondsBadRequest() throws Exception {
        JobDTO invalidDto = new JobDTO();
        invalidDto.setId(VALID_JOB_ID);
        invalidDto.setDescription("");

        String json = objectMapper.writeValueAsString(invalidDto);

        when(jobService.updateJob(eq(VALID_JOB_ID), any(JobDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_JOB_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
