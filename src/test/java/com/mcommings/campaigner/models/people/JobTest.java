package com.mcommings.campaigner.models.people;

import com.mcommings.campaigner.models.people.Job;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JobTest {

    @Test
    public void shouldCreateADefaultJob() {
        Job job = new Job();
        Assertions.assertNotNull(job);
        Assertions.assertEquals(0, job.getId());
        Assertions.assertNull(job.getName());
        Assertions.assertNull(job.getDescription());
    }

    @Test
    public void shouldCreateACustomJob() {
        int id = 1;
        String name = "Custom Job";
        String description = "This is a custom Job.";

        Job job = new Job(id, name, description);

        Assertions.assertEquals(id, job.getId());
        Assertions.assertEquals(name, job.getName());
        Assertions.assertEquals(description, job.getDescription());
    }
    @Test
    public void shouldConvertJobToString() {
        int id = 1;
        String name = "Custom Job";
        String description = "This is a custom Job.";

        Job job = new Job(id, name, description);
        String expected = "Job(super=BaseEntity(name=Custom Job, description=This is a custom Job.), id=1)";

        Assertions.assertEquals(expected, job.toString());
    }
}
