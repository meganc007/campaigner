package com.mcommings.campaigner.models.locations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mcommings.campaigner.models.BaseEntity;
import com.mcommings.campaigner.models.Campaign;
import com.mcommings.campaigner.models.Government;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "countries")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Country extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @Column(name = "fk_continent")
    private Integer fk_continent;

    @Column(name = "fk_government")
    private Integer fk_government;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_continent", referencedColumnName = "id", updatable = false, insertable = false)
    private Continent continent;

    @ManyToOne
    @JoinColumn(name = "fk_government", referencedColumnName = "id", updatable = false, insertable = false)
    private Government government;

    public Country() {
        super();
    }

    public Country(int id, String name, String description, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public Country(int id, String name, String description, UUID fk_campaign_uuid, Integer fk_continent, Integer fk_government) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
        this.fk_continent = fk_continent;
        this.fk_government = fk_government;
    }
}
