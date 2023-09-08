package com.mcommings.campaigner.models.quests;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class CommonQuestEntity {

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;
}
