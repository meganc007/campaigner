package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.modules.people.dtos.JobAssignmentDTO;
import com.mcommings.campaigner.modules.people.entities.JobAssignment;
import com.mcommings.campaigner.modules.people.mappers.JobAssignmentMapper;
import com.mcommings.campaigner.modules.people.repositories.IJobAssignmentRepository;
import com.mcommings.campaigner.modules.people.services.JobAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JobAssignmentTest {
    @Mock
    private JobAssignmentMapper jobAssignmentMapper;
    
    @Mock
    private IJobAssignmentRepository jobAssignmentRepository;

    @InjectMocks
    private JobAssignmentService jobAssignmentService;

    private JobAssignment entity;
    private JobAssignmentDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new JobAssignment();
        entity.setId(1);
        entity.setFk_job(random.nextInt(100) + 1);
        entity.setFk_person(random.nextInt(100) + 1);
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new JobAssignmentDTO();
        dto.setId(entity.getId());
        dto.setFk_job(entity.getFk_job());
        dto.setFk_person(entity.getFk_person());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());

        when(jobAssignmentMapper.mapToJobAssignmentDto(entity)).thenReturn(dto);
        when(jobAssignmentMapper.mapFromJobAssignmentDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreJobAssignments_getJobAssignments_ReturnsJobAssignments() {
        when(jobAssignmentRepository.findAll()).thenReturn(List.of(entity));
        List<JobAssignmentDTO> result = jobAssignmentService.getJobAssignments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    public void whenThereAreNoJobAssignments_getJobAssignments_ReturnsNothing() {
        when(jobAssignmentRepository.findAll()).thenReturn(Collections.emptyList());

        List<JobAssignmentDTO> result = jobAssignmentService.getJobAssignments();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no jobAssignments.");
    }

    @Test
    public void whenThereIsAJobAssignment_getJobAssignment_ReturnsJobAssignment() {
        when(jobAssignmentRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<JobAssignmentDTO> result = jobAssignmentService.getJobAssignment(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void whenThereIsNotAJobAssignment_getJobAssignment_ReturnsJobAssignment() {
        when(jobAssignmentRepository.findById(999)).thenReturn(Optional.empty());

        Optional<JobAssignmentDTO> result = jobAssignmentService.getJobAssignment(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when jobAssignment is not found.");
    }

    @Test
    public void whenCampaignUUIDIsValid_getJobAssignmentsByCampaignUUID_ReturnsJobAssignments() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(jobAssignmentRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<JobAssignmentDTO> result = jobAssignmentService.getJobAssignmentsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getJobAssignmentsByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(jobAssignmentRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<JobAssignmentDTO> result = jobAssignmentService.getJobAssignmentsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no jobAssignments match the campaign UUID.");
    }

    @Test
    public void whenJobAssignmentIsValid_saveJobAssignment_SavesTheJobAssignment() {
        when(jobAssignmentRepository.save(entity)).thenReturn(entity);

        jobAssignmentService.saveJobAssignment(dto);

        verify(jobAssignmentRepository, times(1)).save(entity);
    }

    @Test
    public void whenJobAssignmentAlreadyExists_saveJobAssignment_ThrowsDataIntegrityViolationException() {
        JobAssignment conflictingEntity = new JobAssignment();
        conflictingEntity.setId(dto.getId() + 1);

        when(jobAssignmentMapper.mapFromJobAssignmentDto(dto)).thenReturn(entity);
        when(jobAssignmentRepository.jobAssignmentExists(entity)).thenReturn(Optional.of(conflictingEntity));

        assertThrows(DataIntegrityViolationException.class, () -> jobAssignmentService.saveJobAssignment(dto));

        verify(jobAssignmentRepository, times(1)).jobAssignmentExists(entity);
        verify(jobAssignmentRepository, never()).save(any(JobAssignment.class));
    }

    @Test
    public void whenJobAssignmentIdExists_deleteJobAssignment_DeletesTheJobAssignment() {
        when(jobAssignmentRepository.existsById(1)).thenReturn(true);
        jobAssignmentService.deleteJobAssignment(1);
        verify(jobAssignmentRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenJobAssignmentIdDoesNotExist_deleteJobAssignment_ThrowsIllegalArgumentException() {
        when(jobAssignmentRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> jobAssignmentService.deleteJobAssignment(999));
    }

    @Test
    public void whenDeleteJobAssignmentFails_deleteJobAssignment_ThrowsException() {
        when(jobAssignmentRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(jobAssignmentRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> jobAssignmentService.deleteJobAssignment(1));
    }

    @Test
    public void whenJobAssignmentIdIsFound_updateJobAssignment_UpdatesTheJobAssignment() {
        JobAssignmentDTO updateDTO = new JobAssignmentDTO();
        updateDTO.setFk_job(99);

        when(jobAssignmentRepository.findById(1)).thenReturn(Optional.of(entity));
        when(jobAssignmentRepository.existsById(1)).thenReturn(true);
        when(jobAssignmentRepository.save(entity)).thenReturn(entity);
        when(jobAssignmentMapper.mapToJobAssignmentDto(entity)).thenReturn(updateDTO);

        Optional<JobAssignmentDTO> result = jobAssignmentService.updateJobAssignment(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals(99, result.get().getFk_job());
    }

    @Test
    public void whenJobAssignmentIdIsNotFound_updateJobAssignment_ReturnsEmptyOptional() {
        JobAssignmentDTO updateDTO = new JobAssignmentDTO();
        updateDTO.setFk_person(22);

        when(jobAssignmentRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> jobAssignmentService.updateJobAssignment(999, updateDTO));
    }

    @Test
    public void whenJobAssignmentAlreadyExists_updateJobAssignment_ThrowsDataIntegrityViolationException() {
        JobAssignment conflictingEntity = new JobAssignment();
        conflictingEntity.setId(dto.getId() + 1);

        when(jobAssignmentMapper.mapFromJobAssignmentDto(dto)).thenReturn(entity);
        when(jobAssignmentRepository.jobAssignmentExists(entity)).thenReturn(Optional.of(conflictingEntity));
        when(jobAssignmentRepository.existsById(dto.getId())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> jobAssignmentService.updateJobAssignment(dto.getId(), dto));

        verify(jobAssignmentRepository, times(1)).jobAssignmentExists(entity);
        verify(jobAssignmentRepository, never()).save(any(JobAssignment.class));
    }
}
