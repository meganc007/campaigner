package com.mcommings.campaigner.models.quests;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "objectives")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Objective extends CommonQuestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Objective() {
        super();
    }

    public Objective(int id, String description) {
        this.id = id;
        this.setDescription(description);
    }

    public Objective(int id, String description, String notes) {
        this.id = id;
        this.setDescription(description);
        this.setNotes(notes);
    }
}
