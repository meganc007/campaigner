package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "cities")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class City extends BaseEntity {

    //TODO: add fk_region


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="fk_wealth")
    private Integer fk_wealth;

    @Column(name="fk_country")
    private Integer fk_country;

    @Column(name="fk_settlement")
    private Integer fk_settlement;

    @Column(name="fk_government")
    private Integer fk_government;

    @ManyToOne
    @JoinColumn(name = "fk_wealth", referencedColumnName = "id", updatable = false, insertable = false)
    private Wealth wealth;

    @ManyToOne
    @JoinColumn(name = "fk_country", referencedColumnName = "id", updatable = false, insertable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "fk_settlement", referencedColumnName = "id", updatable = false, insertable = false)
    private SettlementType settlementType;

    @ManyToOne
    @JoinColumn(name = "fk_government", referencedColumnName = "id", updatable = false, insertable = false)
    private Government government;

    public City() {
        super();
    }

    public City(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }

    public City(int id, String name, String description, Integer fk_wealth,
                   Integer fk_country, Integer fk_settlement, Integer fk_government) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_wealth = fk_wealth;
        this.fk_country = fk_country;
        this.fk_settlement = fk_settlement;
        this.fk_government = fk_government;
    }

}
