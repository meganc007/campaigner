package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.modules.people.entities.JobAssignment;
import com.mcommings.campaigner.modules.people.repositories.IJobAssignmentRepository;
import com.mcommings.campaigner.modules.people.repositories.IJobRepository;
import com.mcommings.campaigner.modules.people.repositories.IPersonRepository;
import com.mcommings.campaigner.modules.people.services.JobAssignmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JobAssignmentTest {
    @Mock
    private IJobAssignmentRepository jobAssignmentRepository;
    @Mock
    private IPersonRepository personRepository;
    @Mock
    private IJobRepository jobRepository;

    @InjectMocks
    private JobAssignmentService jobAssignmentService;

    @Test
    public void whenThereAreJobAssignments_getJobAssignments_ReturnsJobAssignments() {
        UUID campaign = UUID.randomUUID();
        List<JobAssignment> jobAssignments = new ArrayList<>();
        jobAssignments.add(new JobAssignment(1, 1, 3, campaign));
        jobAssignments.add(new JobAssignment(2, 2, 6, campaign));
        jobAssignments.add(new JobAssignment(3, 3, 9, campaign));
        when(jobAssignmentRepository.findAll()).thenReturn(jobAssignments);

        List<JobAssignment> result = jobAssignmentService.getJobAssignments();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(jobAssignments, result);
    }

    @Test
    public void whenThereAreNoJobAssignments_getJobAssignments_ReturnsNothing() {
        List<JobAssignment> jobAssignments = new ArrayList<>();
        when(jobAssignmentRepository.findAll()).thenReturn(jobAssignments);

        List<JobAssignment> result = jobAssignmentService.getJobAssignments();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(jobAssignments, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getJobAssignmentsByCampaignUUID_ReturnsJobAssignments() {
        UUID campaign = UUID.randomUUID();
        List<JobAssignment> jobAssignments = new ArrayList<>();
        jobAssignments.add(new JobAssignment(1, 1, 3, campaign));
        jobAssignments.add(new JobAssignment(2, 2, 6, campaign));
        jobAssignments.add(new JobAssignment(3, 3, 9, campaign));
        when(jobAssignmentRepository.findByfk_campaign_uuid(campaign)).thenReturn(jobAssignments);

        List<JobAssignment> results = jobAssignmentService.getJobAssignmentsByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(jobAssignments, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getJobAssignmentsByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<JobAssignment> jobAssignments = new ArrayList<>();
        when(jobAssignmentRepository.findByfk_campaign_uuid(campaign)).thenReturn(jobAssignments);

        List<JobAssignment> result = jobAssignmentService.getJobAssignmentsByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(jobAssignments, result);
    }

    @Test
    public void whenJobAssignmentIsValid_saveJobAssignment_SavesTheJobAssignment() {
        JobAssignment jobAssignment = new JobAssignment(1, 2, 3, UUID.randomUUID());

        when(personRepository.existsById(2)).thenReturn(true);
        when(jobRepository.existsById(3)).thenReturn(true);
        when(jobAssignmentRepository.saveAndFlush(jobAssignment)).thenReturn(jobAssignment);

        assertDoesNotThrow(() -> jobAssignmentService.saveJobAssignment(jobAssignment));

        verify(jobAssignmentRepository, times(1)).saveAndFlush(jobAssignment);
    }

    @Test
    public void whenJobAssignmentAlreadyExists_saveJobAssignment_ThrowsDataIntegrityViolationException() {
        UUID campaign = UUID.randomUUID();
        JobAssignment jobAssignment = new JobAssignment(1, 2, 3, campaign);
        JobAssignment jobAssignmentCopy = new JobAssignment(2, 2, 3, campaign);

        when(personRepository.existsById(2)).thenReturn(true);
        when(jobRepository.existsById(3)).thenReturn(true);

        when(jobAssignmentRepository.saveAndFlush(jobAssignment)).thenReturn(jobAssignment);
        when(jobAssignmentRepository.saveAndFlush(jobAssignmentCopy)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> jobAssignmentService.saveJobAssignment(jobAssignment));
        assertThrows(DataIntegrityViolationException.class, () -> jobAssignmentService.saveJobAssignment(jobAssignmentCopy));
    }

    @Test
    public void whenJobAssignmentIdExists_deleteJobAssignment_DeletesTheJobAssignment() {
        int jobAssignmentId = 1;
        when(jobAssignmentRepository.existsById(jobAssignmentId)).thenReturn(true);
        assertDoesNotThrow(() -> jobAssignmentService.deleteJobAssignment(jobAssignmentId));
        verify(jobAssignmentRepository, times(1)).deleteById(jobAssignmentId);
    }

    @Test
    public void whenJobAssignmentIdDoesNotExist_deleteJobAssignment_ThrowsIllegalArgumentException() {
        int jobAssignmentId = 9000;
        when(jobAssignmentRepository.existsById(jobAssignmentId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> jobAssignmentService.deleteJobAssignment(jobAssignmentId));
    }

    //TODO: test delete when JobAssignment is a fk

    @Test
    public void whenJobAssignmentIdWithValidFKIsFound_updateJobAssignment_UpdatesTheJobAssignment() {
        int jaId = 1;
        UUID campaign = UUID.randomUUID();
        JobAssignment jobAssignment = new JobAssignment(jaId, 2, 3, campaign);
        JobAssignment update = new JobAssignment(jaId, 3, 4, campaign);

        when(jobAssignmentRepository.existsById(jaId)).thenReturn(true);
        when(jobAssignmentRepository.findById(jaId)).thenReturn(Optional.of(jobAssignment));
        when(personRepository.existsById(2)).thenReturn(true);
        when(jobRepository.existsById(3)).thenReturn(true);
        when(personRepository.existsById(3)).thenReturn(true);
        when(jobRepository.existsById(4)).thenReturn(true);

        jobAssignmentService.updateJobAssignment(jaId, update);

        verify(jobAssignmentRepository).findById(jaId);

        JobAssignment result = jobAssignmentRepository.findById(jaId).get();
        Assertions.assertEquals(update.getId(), result.getId());
        Assertions.assertEquals(update.getFk_person(), result.getFk_person());
        Assertions.assertEquals(update.getFk_job(), result.getFk_job());
    }

    @Test
    public void whenJobAssignmentIdWithInvalidFKIsFound_updateJobAssignment_ThrowsDataIntegrityViolationException() {
        int jaId = 1;
        UUID campaign = UUID.randomUUID();
        JobAssignment jobAssignment = new JobAssignment(jaId, 2, 3, campaign);
        JobAssignment update = new JobAssignment(jaId, 3, 4, campaign);

        when(jobAssignmentRepository.existsById(jaId)).thenReturn(true);
        when(jobAssignmentRepository.findById(jaId)).thenReturn(Optional.of(jobAssignment));
        when(personRepository.existsById(2)).thenReturn(true);
        when(jobRepository.existsById(3)).thenReturn(false);
        when(personRepository.existsById(3)).thenReturn(true);
        when(jobRepository.existsById(4)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> jobAssignmentService.updateJobAssignment(jaId, update));
    }

    @Test
    public void whenJobAssignmentIdIsNotFound_updateJobAssignment_ThrowsIllegalArgumentException() {
        int jaId = 1;
        JobAssignment jobAssignment = new JobAssignment(jaId, 2, 3, UUID.randomUUID());

        when(jobAssignmentRepository.existsById(jaId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> jobAssignmentService.updateJobAssignment(jaId, jobAssignment));
    }

    @Test
    public void whenSomeJobAssignmentFieldsChanged_updateJobAssignment_OnlyUpdatesChangedFields() {
        int jaId = 1;
        JobAssignment jobAssignment = new JobAssignment(jaId, 2, 3, UUID.randomUUID());
        int newJob = 9;

        JobAssignment jobAssignmentToUpdate = new JobAssignment();
        jobAssignmentToUpdate.setFk_job(newJob);

        when(jobAssignmentRepository.existsById(jaId)).thenReturn(true);
        when(personRepository.existsById(2)).thenReturn(true);
        when(jobRepository.existsById(newJob)).thenReturn(true);
        when(jobAssignmentRepository.findById(jaId)).thenReturn(Optional.of(jobAssignment));

        jobAssignmentService.updateJobAssignment(jaId, jobAssignmentToUpdate);

        verify(jobAssignmentRepository).findById(jaId);

        JobAssignment result = jobAssignmentRepository.findById(jaId).get();
        Assertions.assertEquals(jobAssignment.getFk_person(), result.getFk_person());
        Assertions.assertEquals(newJob, result.getFk_job());
    }
}
