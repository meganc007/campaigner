package com.mcommings.campaigner.models.items;

import com.mcommings.campaigner.models.locations.Place;
import com.mcommings.campaigner.models.people.Person;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "inventory")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_person")
    private Integer fk_person;

    @Column(name = "fk_item")
    private Integer fk_item;

    @Column(name = "fk_weapon")
    private Integer fk_weapon;

    @Column(name = "fk_place")
    private Integer fk_place;


    @ManyToOne
    @JoinColumn(name = "fk_person", referencedColumnName = "id", updatable = false, insertable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "fk_item", referencedColumnName = "id", updatable = false, insertable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "fk_weapon", referencedColumnName = "id", updatable = false, insertable = false)
    private Weapon weapon;

    @ManyToOne
    @JoinColumn(name = "fk_place", referencedColumnName = "id", updatable = false, insertable = false)
    private Place place;

    public Inventory(int id, Integer fk_person, Integer fk_item, Integer fk_weapon, Integer fk_place) {
        this.id = id;
        this.fk_person = fk_person;
        this.fk_item = fk_item;
        this.fk_weapon = fk_weapon;
        this.fk_place = fk_place;
    }

}
