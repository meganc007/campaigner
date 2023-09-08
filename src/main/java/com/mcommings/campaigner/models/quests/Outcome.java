package com.mcommings.campaigner.models.quests;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "outcomes")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Outcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;

    public Outcome() {
    }

    public Outcome(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public Outcome(int id, String description, String notes) {
        this.id = id;
        this.description = description;
        this.notes = notes;
    }
}
