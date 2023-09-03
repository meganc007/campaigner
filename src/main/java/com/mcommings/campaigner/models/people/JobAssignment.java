package com.mcommings.campaigner.models.people;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "job_assignment")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class JobAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_person")
    private Integer fk_person;

    @Column(name = "fk_job")
    private Integer fk_job;

    @ManyToOne
    @JoinColumn(name = "fk_person", referencedColumnName = "id", updatable = false, insertable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "fk_job", referencedColumnName = "id", updatable = false, insertable = false)
    private Job job;

    public JobAssignment() {
    }

    public JobAssignment(int id, Integer fk_person, Integer fk_job) {
        this.id = id;
        this.fk_person = fk_person;
        this.fk_job = fk_job;
    }

}
