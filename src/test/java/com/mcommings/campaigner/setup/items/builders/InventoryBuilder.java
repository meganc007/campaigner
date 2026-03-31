package com.mcommings.campaigner.setup.items.builders;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.items.entities.Inventory;
import com.mcommings.campaigner.modules.items.entities.Item;
import com.mcommings.campaigner.modules.items.entities.Weapon;
import com.mcommings.campaigner.modules.locations.entities.Place;
import com.mcommings.campaigner.modules.people.entities.Person;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

import java.util.UUID;

public class InventoryBuilder {

    private int id = ItemsTestConstants.INVENTORY_ID;
    private UUID campaignUuid = ItemsTestConstants.CAMPAIGN_UUID;
    private int personId = ItemsTestConstants.PERSON_ID;
    private int itemId = ItemsTestConstants.ITEM_ID;
    private int weaponId = ItemsTestConstants.WEAPON_ID;
    private int placeId = LocationsTestConstants.PLACE_ID;

    public static InventoryBuilder aInventory() {
        return new InventoryBuilder();
    }

    public InventoryBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public InventoryBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public InventoryBuilder withPersonId(int personId) {
        this.personId = personId;
        return this;
    }

    public InventoryBuilder withItemId(int itemId) {
        this.itemId = itemId;
        return this;
    }

    public InventoryBuilder withWeaponId(int weaponId) {
        this.weaponId = weaponId;
        return this;
    }

    public InventoryBuilder withPlaceId(int placeId) {
        this.placeId = placeId;
        return this;
    }

    public Inventory build() {
        Inventory inventory = new Inventory();
        inventory.setId(id);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        inventory.setCampaign(campaign);

        Person person = new Person();
        person.setId(personId);
        inventory.setPerson(person);

        Item item = new Item();
        item.setId(itemId);
        inventory.setItem(item);

        Weapon weapon = new Weapon();
        weapon.setId(weaponId);
        inventory.setWeapon(weapon);

        Place place = new Place();
        place.setId(placeId);
        inventory.setPlace(place);

        return inventory;
    }
}
