package com.mcommings.campaigner.modules.people.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "job_assignment")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@AllArgsConstructor
public class JobAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Integer fk_person;
    private Integer fk_job;
    @Column(nullable = false)
    private UUID fk_campaign_uuid;
}
