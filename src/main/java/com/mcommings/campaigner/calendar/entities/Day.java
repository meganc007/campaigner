package com.mcommings.campaigner.calendar.entities;

import com.mcommings.campaigner.entities.BaseEntity;
import com.mcommings.campaigner.entities.Campaign;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

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

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_week", referencedColumnName = "id", updatable = false, insertable = false)
    private Week week;

    public Day() {
        super();
    }

    public Day(int id, String name, String description, Integer fk_week, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_week = fk_week;
        this.fk_campaign_uuid = fk_campaign_uuid;
    }
}
