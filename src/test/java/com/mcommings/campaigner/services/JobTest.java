package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Job;
import com.mcommings.campaigner.repositories.IJobRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JobTest {

    @Mock
    private IJobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @Test
    public void whenThereAreJobs_getJobs_ReturnsJobs() {
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job(1, "Job 1", "Description 1"));
        jobs.add(new Job(2, "Job 2", "Description 2"));
        when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> result = jobService.getJobs();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(jobs, result);
    }

    @Test
    public void whenThereAreNoJobs_getJobs_ReturnsNothing() {
        List<Job> jobs = new ArrayList<>();
        when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> result = jobService.getJobs();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(jobs, result);
    }

    @Test
    public void whenJobIsValid_saveJob_SavesTheJob() {
        Job job = new Job(1, "Job 1", "Description 1");
        when(jobRepository.saveAndFlush(job)).thenReturn(job);

        assertDoesNotThrow(() -> jobService.saveJob(job));
        verify(jobRepository, times(1)).saveAndFlush(job);
    }

    @Test
    public void whenJobNameIsInvalid_saveJob_ThrowsIllegalArgumentException() {
        Job jobWithEmptyName = new Job(1, "", "Description 1");
        Job jobWithNullName = new Job(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> jobService.saveJob(jobWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> jobService.saveJob(jobWithNullName));
    }

    @Test
    public void whenJobNameAlreadyExists_saveJob_ThrowsDataIntegrityViolationException() {
        Job job = new Job(1, "Job 1", "Description 1");
        Job jobWithDuplicatedName = new Job(2, "Job 1", "Description 2");
        when(jobRepository.saveAndFlush(job)).thenReturn(job);
        when(jobRepository.saveAndFlush(jobWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> jobService.saveJob(job));
        assertThrows(DataIntegrityViolationException.class, () -> jobService.saveJob(jobWithDuplicatedName));
    }

    @Test
    public void whenJobIdExists_deleteJob_DeletesTheJob() {
        int jobId = 1;
        when(jobRepository.existsById(jobId)).thenReturn(true);
        assertDoesNotThrow(() -> jobService.deleteJob(jobId));
        verify(jobRepository, times(1)).deleteById(jobId);
    }

    @Test
    public void whenJobIdDoesNotExist_deleteJob_ThrowsIllegalArgumentException() {
        int jobId = 9000;
        when(jobRepository.existsById(jobId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> jobService.deleteJob(jobId));
    }

    //TODO: test that deleteJob doesn't delete when it's a foreign key

    @Test
    public void whenJobIdIsFound_updateJob_UpdatesTheJob() {
        int jobId = 1;
        Job job = new Job(jobId, "Old Job Name", "Old Description");
        Job jobToUpdate = new Job(jobId, "Updated Job Name", "Updated Description");

        when(jobRepository.existsById(jobId)).thenReturn(true);
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        jobService.updateJob(jobId, jobToUpdate);

        verify(jobRepository).findById(jobId);

        Job result = jobRepository.findById(jobId).get();
        Assertions.assertEquals(jobToUpdate.getName(), result.getName());
        Assertions.assertEquals(jobToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenJobIdIsNotFound_updateJob_ThrowsIllegalArgumentException() {
        int jobId = 1;
        Job job = new Job(jobId, "Old Job Name", "Old Description");

        when(jobRepository.existsById(jobId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> jobService.updateJob(jobId, job));
    }
}
