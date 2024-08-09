package com.mcommings.campaigner.models.locations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mcommings.campaigner.models.BaseEntity;
import com.mcommings.campaigner.models.Campaign;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Entity
@Table(name = "landmarks")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Landmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @Column(name = "fk_region")
    @JsonBackReference
    private Integer fk_region;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_region", referencedColumnName = "id", updatable = false, insertable = false)
    private Region region;

    public Landmark() {
        super();
    }

    public Landmark(int id, String name, String description, UUID fk_campaign_uuid) {
        this.id = id;
        super.setName(name);
        super.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public Landmark(int id, String name, String description, UUID fk_campaign_uuid, Integer fk_region) {
        this.id = id;
        super.setName(name);
        super.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
        this.fk_region = fk_region;
    }
}
