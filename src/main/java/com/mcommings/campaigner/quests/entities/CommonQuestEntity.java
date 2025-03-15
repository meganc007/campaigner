package com.mcommings.campaigner.quests.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class CommonQuestEntity {

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;
}
