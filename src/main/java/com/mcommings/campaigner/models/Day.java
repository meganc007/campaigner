package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "days")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Day extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_week")
    private Integer fk_week;

    @ManyToOne
    @JoinColumn(name = "fk_week", referencedColumnName = "id", updatable = false, insertable = false)
    private Week week;

    public Day() {
        super();
    }

    public Day(int id, String name, String description, Integer fk_week) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_week = fk_week;
    }
}
