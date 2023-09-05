package com.mcommings.campaigner.models.people;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class SentientBeingEntity {

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName")
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
