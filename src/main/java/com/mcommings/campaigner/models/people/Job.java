package com.mcommings.campaigner.models.people;

import com.mcommings.campaigner.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "jobs")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Job(){
        super();
    }

    public Job(int id, String name, String description){
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }
}
