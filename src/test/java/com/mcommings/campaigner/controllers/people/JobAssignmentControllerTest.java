package com.mcommings.campaigner.controllers.people;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.people.controllers.JobAssignmentController;
import com.mcommings.campaigner.modules.people.dtos.JobAssignmentDTO;
import com.mcommings.campaigner.modules.people.entities.JobAssignment;
import com.mcommings.campaigner.modules.people.services.JobAssignmentService;
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

@WebMvcTest(JobAssignmentController.class)
public class JobAssignmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    JobAssignmentService jobAssignmentService;

    private static final int VALID_JOB_ASSIGNMENT_ID = 1;
    private static final int INVALID_JOB_ASSIGNMENT_ID = 999;
    private static final String URI = "/api/jobAssignments";
    private JobAssignment entity;
    private JobAssignmentDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new JobAssignment();
        entity.setId(VALID_JOB_ASSIGNMENT_ID);
        entity.setFk_job(random.nextInt(100) + 1);
        entity.setFk_person(random.nextInt(100) + 1);
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new JobAssignmentDTO();
        dto.setId(entity.getId());
        dto.setFk_job(entity.getFk_job());
        dto.setFk_person(entity.getFk_person());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
    }

    @Test
    void whenThereAreJobAssignments_getJobAssignments_ReturnsJobAssignments() throws Exception {
        when(jobAssignmentService.getJobAssignments()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreJobAssignments_getJobAssignments_ReturnsEmptyList() throws Exception {
        when(jobAssignmentService.getJobAssignments()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAJobAssignment_getJobAssignment_ReturnsJobAssignment() throws Exception {
        when(jobAssignmentService.getJobAssignment(VALID_JOB_ASSIGNMENT_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_JOB_ASSIGNMENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_JOB_ASSIGNMENT_ID));
    }

    @Test
    void whenThereIsNotAJobAssignment_getJobAssignment_ThrowsIllegalArgumentException() throws Exception {
        when(jobAssignmentService.getJobAssignment(INVALID_JOB_ASSIGNMENT_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_JOB_ASSIGNMENT_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getJobAssignment_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getJobAssignment_ReturnsInternalServerError() throws Exception {
        when(jobAssignmentService.getJobAssignment(VALID_JOB_ASSIGNMENT_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_JOB_ASSIGNMENT_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getJobAssignment_ReturnsInternalServerError() throws Exception {
        when(jobAssignmentService.getJobAssignment(VALID_JOB_ASSIGNMENT_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_JOB_ASSIGNMENT_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getJobAssignmentsByCampaignUUID_ReturnsJobAssignments() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(jobAssignmentService.getJobAssignmentsByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getJobAssignmentsByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(jobAssignmentService.getJobAssignmentsByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getJobAssignment_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPersonIdIsValid_getJobAssignmentsByPersonId_ReturnsJobAssignments() throws Exception {
        int personId = random.nextInt(100) + 1;
        when(jobAssignmentService.getJobAssignmentsByPersonId(personId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/person/" + personId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenPersonIdIsNotValid_getJobAssignmentsByPersonId_ReturnsEmptyList() throws Exception {
        int personId = random.nextInt(100) + 1;
        when(jobAssignmentService.getJobAssignmentsByPersonId(personId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/person/" + personId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenJobIdIsValid_getJobAssignmentsByJobId_ReturnsJobAssignments() throws Exception {
        int jobId = random.nextInt(100) + 1;
        when(jobAssignmentService.getJobAssignmentsByJobId(jobId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/job/" + jobId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenJobIdIsNotValid_getJobAssignmentsByJobId_ReturnsEmptyList() throws Exception {
        int jobId = random.nextInt(100) + 1;
        when(jobAssignmentService.getJobAssignmentsByJobId(jobId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/job/" + jobId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenJobAssignmentIsValid_saveJobAssignment_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        JobAssignmentDTO requestDto = new JobAssignmentDTO();
        requestDto.setId(2);
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_job(99);
        requestDto.setFk_person(99);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/jobAssignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(jobAssignmentService, times(1)).saveJobAssignment(any(JobAssignmentDTO.class));
    }

    @Test
    void whenJobAssignmentIsNotValid_saveJobAssignment_RespondsBadRequest() throws Exception {
        JobAssignmentDTO invalidJobAssignment = new JobAssignmentDTO();
        invalidJobAssignment.setId(2);
        invalidJobAssignment.setFk_campaign_uuid(null);
        invalidJobAssignment.setFk_job(0);
        invalidJobAssignment.setFk_person(0);

        String requestJson = objectMapper.writeValueAsString(invalidJobAssignment);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(jobAssignmentService).saveJobAssignment(any(JobAssignmentDTO.class));

        MvcResult result = mockMvc.perform(post("/api/jobAssignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));
        assertTrue(errors.contains("Person ID must be greater than zero."));
        assertTrue(errors.contains("Job ID must be greater than zero."));

        verify(jobAssignmentService, times(0)).saveJobAssignment(any(JobAssignmentDTO.class));
    }

    @Test
    void whenJobAssignmentIdIsValid_deleteJobAssignment_RespondsOkRequest() throws Exception {
        when(jobAssignmentService.getJobAssignment(VALID_JOB_ASSIGNMENT_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_JOB_ASSIGNMENT_ID))
                .andExpect(status().isOk());

        verify(jobAssignmentService, times(1)).deleteJobAssignment(VALID_JOB_ASSIGNMENT_ID);
    }

    @Test
    void whenJobAssignmentIdIsInvalid_deleteJobAssignment_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(jobAssignmentService).deleteJobAssignment(INVALID_JOB_ASSIGNMENT_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_JOB_ASSIGNMENT_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(jobAssignmentService, times(1)).deleteJobAssignment(INVALID_JOB_ASSIGNMENT_ID);
    }

    @Test
    void whenJobAssignmentIdIsValid_updateJobAssignment_RespondsOkRequest() throws Exception {
        JobAssignmentDTO updatedDto = new JobAssignmentDTO();
        updatedDto.setId(VALID_JOB_ASSIGNMENT_ID);
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_job(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_JOB_ASSIGNMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(jobAssignmentService, times(1)).updateJobAssignment(eq(VALID_JOB_ASSIGNMENT_ID), any(JobAssignmentDTO.class));
    }

    @Test
    void whenJobAssignmentIdIsInvalid_updateJobAssignment_RespondsBadRequest() throws Exception {
        JobAssignmentDTO updatedDto = new JobAssignmentDTO();
        updatedDto.setId(INVALID_JOB_ASSIGNMENT_ID);
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_person(0);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(jobAssignmentService).updateJobAssignment(eq(INVALID_JOB_ASSIGNMENT_ID), any(JobAssignmentDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_JOB_ASSIGNMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }
}
