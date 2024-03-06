package com.mcommings.campaigner.models.calendar;

import com.mcommings.campaigner.models.Campaign;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

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

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_month", referencedColumnName = "id", updatable = false, insertable = false)
    private Month month;

    public Week() {
    }

    public Week(int id, String description, Integer week_number, Integer fk_month, UUID fk_campaign_uuid) {
        this.id = id;
        this.setDescription(description);
        this.week_number = week_number;
        this.fk_month = fk_month;
        this.fk_campaign_uuid = fk_campaign_uuid;
    }
}
