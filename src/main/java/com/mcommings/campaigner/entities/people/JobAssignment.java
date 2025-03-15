package com.mcommings.campaigner.entities.people;

import com.mcommings.campaigner.common.entities.Campaign;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

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

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_person", referencedColumnName = "id", updatable = false, insertable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "fk_job", referencedColumnName = "id", updatable = false, insertable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    public JobAssignment() {
    }

    public JobAssignment(int id, Integer fk_person, Integer fk_job, UUID fk_campaign_uuid) {
        this.id = id;
        this.fk_person = fk_person;
        this.fk_job = fk_job;
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

}
