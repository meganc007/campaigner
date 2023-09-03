package com.mcommings.campaigner.models.people;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JobAssignmentTest {

    @Test
    public void shouldCreateADefaultJobAssignment() {
        JobAssignment jobAssignment = new JobAssignment();
        Assertions.assertNotNull(jobAssignment);
        Assertions.assertEquals(0, jobAssignment.getId());
        Assertions.assertNull(jobAssignment.getFk_person());
        Assertions.assertNull(jobAssignment.getFk_job());
    }

    @Test
    public void shouldCreateACustomJobAssignment() {
        int id = 1;
        Integer fk_person = 3;
        Integer fk_job = 9;

        JobAssignment jobAssignment = new JobAssignment(id, fk_person, fk_job);
        Assertions.assertEquals(id, jobAssignment.getId());
        Assertions.assertEquals(fk_person, jobAssignment.getFk_person());
        Assertions.assertEquals(fk_job, jobAssignment.getFk_job());
    }

    @Test
    public void shouldConvertJobAssignmentToString() {
        int id = 1;
        Integer fk_person = 3;
        Integer fk_job = 9;

        JobAssignment jobAssignment = new JobAssignment(id, fk_person, fk_job);
        String expected = "JobAssignment(id=1, fk_person=3, fk_job=9, person=null, job=null)";

        Assertions.assertEquals(expected, jobAssignment.toString());
    }
}
