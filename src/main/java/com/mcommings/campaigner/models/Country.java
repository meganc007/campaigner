package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "countries")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Country extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="fk_continent")
    private Integer fk_continent;

    @Column(name="fk_government")
    private Integer fk_government;

    @ManyToOne
    @JoinColumn(name = "fk_continent", referencedColumnName = "id", updatable=false, insertable=false)
    private Continent continent;

    @ManyToOne
    @JoinColumn(name = "fk_government", referencedColumnName = "id", updatable=false, insertable=false)
    private Government government;

    public Country() {
        super();
    }

    public Country(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }
    public Country(int id, String name, String description, Integer fk_continent, Integer fk_government) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_continent = fk_continent;
        this.fk_government = fk_government;
    }
}
