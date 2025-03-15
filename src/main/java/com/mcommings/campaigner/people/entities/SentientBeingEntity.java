package com.mcommings.campaigner.people.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class SentientBeingEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "title")
    private String title;

    @Column(name = "isEnemy")
    private Boolean isEnemy;

    @Column(name = "personality")
    private String personality;

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;
}
