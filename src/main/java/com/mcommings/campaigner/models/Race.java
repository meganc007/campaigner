package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "races")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Race extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean is_exotic;

    public Race() {
        super();
    }

    public Race(int id, String name, String description, boolean is_exotic) {
        this.id = id;
        super.setName(name);
        super.setDescription(description);
        this.is_exotic = is_exotic;
    }
}
