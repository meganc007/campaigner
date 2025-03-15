package com.mcommings.campaigner.admin.entities;

import com.mcommings.campaigner.common.entities.Campaign;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "permissions")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_permission_type")
    private Integer fk_permission_type;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @Column(name = "fk_user_uuid")
    private UUID fk_user_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_permission_type", referencedColumnName = "id", updatable = false, insertable = false)
    private PermissionType permissionType;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_user_uuid", referencedColumnName = "user_uuid", updatable = false, insertable = false)
    private User user;

    public Permission() {
        super();
    }

    public Permission(int id, Integer fk_permission_type, UUID fk_campaign_uuid, UUID fk_user_uuid) {
        this.id = id;
        this.fk_permission_type = fk_permission_type;
        this.fk_campaign_uuid = fk_campaign_uuid;
        this.fk_user_uuid = fk_user_uuid;
    }
}
