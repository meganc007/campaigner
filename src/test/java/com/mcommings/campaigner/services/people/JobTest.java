package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.modules.people.dtos.JobDTO;
import com.mcommings.campaigner.modules.people.entities.Job;
import com.mcommings.campaigner.modules.people.mappers.JobMapper;
import com.mcommings.campaigner.modules.people.repositories.IJobRepository;
import com.mcommings.campaigner.modules.people.services.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JobTest {

    @Mock
    private JobMapper jobMapper;

    @Mock
    private IJobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private Job entity;
    private JobDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Job();
        entity.setId(1);
        entity.setName("Job 1");
        entity.setDescription("This is a description");

        dto = new JobDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        when(jobMapper.mapToJobDto(entity)).thenReturn(dto);
        when(jobMapper.mapFromJobDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreJobs_getJobs_ReturnsJobs() {
        when(jobRepository.findAll()).thenReturn(List.of(entity));
        List<JobDTO> result = jobService.getJobs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Job 1", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoJobs_getJobs_ReturnsNothing() {
        when(jobRepository.findAll()).thenReturn(Collections.emptyList());

        List<JobDTO> result = jobService.getJobs();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no jobs.");
    }

    @Test
    public void whenThereIsAJob_getJob_ReturnsJob() {
        when(jobRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<JobDTO> result = jobService.getJob(1);

        assertTrue(result.isPresent());
        assertEquals("Job 1", result.get().getName());
    }

    @Test
    public void whenThereIsNotAJob_getJob_ReturnsJob() {
        when(jobRepository.findById(999)).thenReturn(Optional.empty());

        Optional<JobDTO> result = jobService.getJob(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when job is not found.");
    }

    @Test
    public void whenJobNameIsInvalid_saveJob_ThrowsIllegalArgumentException() {
        JobDTO jobWithEmptyName = new JobDTO();
        jobWithEmptyName.setId(1);
        jobWithEmptyName.setName("");
        jobWithEmptyName.setDescription("A fictional job.");

        JobDTO jobWithNullName = new JobDTO();
        jobWithNullName.setId(1);
        jobWithNullName.setName(null);
        jobWithNullName.setDescription("A fictional city.");
        
        assertThrows(IllegalArgumentException.class, () -> jobService.saveJob(jobWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> jobService.saveJob(jobWithNullName));
    }

    @Test
    public void whenJobNameAlreadyExists_saveJob_ThrowsDataIntegrityViolationException() {
        when(jobRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> jobService.saveJob(dto));
        verify(jobRepository, times(1)).findByName(dto.getName());
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    public void whenJobIdExists_deleteJob_DeletesTheJob() {
        when(jobRepository.existsById(1)).thenReturn(true);
        jobService.deleteJob(1);
        verify(jobRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenJobIdDoesNotExist_deleteJob_ThrowsIllegalArgumentException() {
        when(jobRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> jobService.deleteJob(999));
    }

    @Test
    public void whenDeleteJobFails_deleteJob_ThrowsException() {
        when(jobRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(jobRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> jobService.deleteJob(1));
    }

    @Test
    public void whenJobIdIsFound_updateJob_UpdatesTheJob() {
        JobDTO updateDTO = new JobDTO();
        updateDTO.setName("Updated Name");

        when(jobRepository.findById(1)).thenReturn(Optional.of(entity));
        when(jobRepository.existsById(1)).thenReturn(true);
        when(jobRepository.save(entity)).thenReturn(entity);
        when(jobMapper.mapToJobDto(entity)).thenReturn(updateDTO);

        Optional<JobDTO> result = jobService.updateJob(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    public void whenJobIdIsNotFound_updateJob_ReturnsEmptyOptional() {
        JobDTO updateDTO = new JobDTO();
        updateDTO.setName("Updated Name");

        when(jobRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> jobService.updateJob(999, updateDTO));
    }

    @Test
    public void whenJobNameIsInvalid_updateJob_ThrowsIllegalArgumentException() {
        JobDTO updateEmptyName = new JobDTO();
        updateEmptyName.setName("");

        JobDTO updateNullName = new JobDTO();
        updateNullName.setName(null);

        when(jobRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> jobService.updateJob(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> jobService.updateJob(1, updateNullName));
    }

    @Test
    public void whenJobNameAlreadyExists_updateJob_ThrowsDataIntegrityViolationException() {
        when(jobRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> jobService.updateJob(entity.getId(), dto));
    }
}
