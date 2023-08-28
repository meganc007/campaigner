package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "months")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Month extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "season")
    private String season;

    public Month() {
        super();
    }

    public Month(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }

    public Month(int id, String name, String description, String season) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.season = season;
    }
}
