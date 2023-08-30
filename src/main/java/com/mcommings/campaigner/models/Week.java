package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "weeks")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Week {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "week_number")
    private Integer week_number;

    @Column(name = "fk_month")
    private Integer fk_month;

    @ManyToOne
    @JoinColumn(name = "fk_month", referencedColumnName = "id", updatable = false, insertable = false)
    private Month month;

    public Week() {}

    public Week(int id, String description, Integer week_number, Integer fk_month) {
        this.id = id;
        this.setDescription(description);
        this.week_number = week_number;
        this.fk_month = fk_month;
    }
}
