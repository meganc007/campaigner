package com.mcommings.campaigner.models.locations;

import com.mcommings.campaigner.models.BaseEntity;
import com.mcommings.campaigner.models.Climate;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "regions")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_country")
    private Integer fk_country;

    @Column(name = "fk_climate")
    private Integer fk_climate;

    @ManyToOne
    @JoinColumn(name = "fk_country", referencedColumnName = "id", updatable = false, insertable = false )
    private Country country;

    @ManyToOne
    @JoinColumn(name = "fk_climate", referencedColumnName = "id", updatable = false, insertable = false)
    private Climate climate;

    public Region() {
        super();
    }

    public Region(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }

    public Region(int id, String name, String description, Integer fk_country, Integer fk_climate) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_country = fk_country;
        this.fk_climate = fk_climate;
    }
}
