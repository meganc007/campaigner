package com.mcommings.campaigner.models.locations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mcommings.campaigner.models.BaseEntity;
import com.mcommings.campaigner.models.Campaign;
import com.mcommings.campaigner.models.Government;
import com.mcommings.campaigner.models.Wealth;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "cities")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class City extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @Column(name = "fk_wealth")
    private Integer fk_wealth;

    @Column(name = "fk_country")
    private Integer fk_country;

    @Column(name = "fk_settlement")
    private Integer fk_settlement;

    @Column(name = "fk_government")
    private Integer fk_government;

    @Column(name = "fk_region")
    private Integer fk_region;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_wealth", referencedColumnName = "id", updatable = false, insertable = false)
    private Wealth wealth;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_country", referencedColumnName = "id", updatable = false, insertable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "fk_settlement", referencedColumnName = "id", updatable = false, insertable = false)
    private SettlementType settlementType;

    @ManyToOne
    @JoinColumn(name = "fk_government", referencedColumnName = "id", updatable = false, insertable = false)
    private Government government;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_region", referencedColumnName = "id", updatable = false, insertable = false)
    private Region region;

    @JsonManagedReference
    @OneToMany(mappedBy = "city")
    private Set<Place> places = new HashSet<>();

    public City() {
        super();
    }

    public City(int id, String name, String description, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public City(int id, String name, String description, UUID fk_campaign_uuid, Integer fk_wealth,
                Integer fk_country, Integer fk_settlement, Integer fk_government, Integer fk_region) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
        this.fk_wealth = fk_wealth;
        this.fk_country = fk_country;
        this.fk_settlement = fk_settlement;
        this.fk_government = fk_government;
        this.fk_region = fk_region;
    }

}
