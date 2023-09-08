package com.mcommings.campaigner.models.quests;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "hooks")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Hook extends CommonQuestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Hook() {
        super();
    }

    public Hook(int id, String description) {
        this.id = id;
        this.setDescription(description);
    }

    public Hook(int id, String description, String notes) {
        this.id = id;
        this.setDescription(description);
        this.setNotes(notes);
    }
}
