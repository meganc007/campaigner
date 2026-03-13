package com.mcommings.campaigner.setup.locations.builders;

import com.mcommings.campaigner.modules.locations.entities.SettlementType;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

public class SettlementTypeBuilder {

    private int id = LocationsTestConstants.SETTLEMENTTYPE_ID;
    private String name = LocationsTestConstants.SETTLEMENTTYPE_NAME;
    private String description = LocationsTestConstants.SETTLEMENTTYPE_DESCRIPTION;

    public static SettlementTypeBuilder aSettlementType() {
        return new SettlementTypeBuilder();
    }

    public SettlementTypeBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SettlementTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SettlementTypeBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public SettlementType build() {
        SettlementType settlementType = new SettlementType();
        settlementType.setId(id);
        settlementType.setName(name);
        settlementType.setDescription(description);

        return settlementType;
    }
}
