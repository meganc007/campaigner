package com.mcommings.campaigner.setup.locations.builders;

import com.mcommings.campaigner.modules.locations.entities.PlaceType;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

public class PlaceTypeBuilder {
    private int id = LocationsTestConstants.PLACETYPE_ID;
    private String name = LocationsTestConstants.PLACETYPE_NAME;
    private String description = LocationsTestConstants.PLACETYPE_DESCRIPTION;

    public static PlaceTypeBuilder aPlaceType() {
        return new PlaceTypeBuilder();
    }

    public PlaceTypeBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public PlaceTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PlaceTypeBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PlaceType build() {
        PlaceType placeType = new PlaceType();
        placeType.setId(id);
        placeType.setName(name);
        placeType.setDescription(description);

        return placeType;
    }
}
