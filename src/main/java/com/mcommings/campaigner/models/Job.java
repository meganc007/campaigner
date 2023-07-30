package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @SequenceGenerator(name = "jobs_id_seq", sequenceName = "jobs_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobs_id_seq")
    private int id;
    private String name;
    private String description;

    public Job(){}

    public Job(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
