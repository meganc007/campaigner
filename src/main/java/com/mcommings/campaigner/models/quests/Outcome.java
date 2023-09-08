package com.mcommings.campaigner.models.quests;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "outcomes")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Outcome extends CommonQuestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Outcome() {
        super();
    }

    public Outcome(int id, String description) {
        this.id = id;
        this.setDescription(description);
    }

    public Outcome(int id, String description, String notes) {
        this.id = id;
        this.setDescription(description);
        this.setNotes(notes);
    }
}
