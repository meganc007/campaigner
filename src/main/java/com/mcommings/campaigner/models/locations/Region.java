package com.mcommings.campaigner.models.locations;

import com.mcommings.campaigner.models.BaseEntity;
import com.mcommings.campaigner.models.Campaign;
import com.mcommings.campaigner.models.Climate;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Entity
@Table(name = "regions")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @Column(name = "fk_country")
    private Integer fk_country;

    @Column(name = "fk_climate")
    private Integer fk_climate;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_country", referencedColumnName = "id", updatable = false, insertable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "fk_climate", referencedColumnName = "id", updatable = false, insertable = false)
    private Climate climate;

    public Region() {
        super();
    }

    public Region(int id, String name, String description, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public Region(int id, String name, String description, UUID fk_campaign_uuid, Integer fk_country, Integer fk_climate) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
        this.fk_country = fk_country;
        this.fk_climate = fk_climate;
    }
}
