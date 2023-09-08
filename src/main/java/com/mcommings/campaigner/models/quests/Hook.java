package com.mcommings.campaigner.models.quests;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hooks")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Hook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;

    public Hook() {
    }

    public Hook(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public Hook(int id, String description, String notes) {
        this.id = id;
        this.description = description;
        this.notes = notes;
    }
}
