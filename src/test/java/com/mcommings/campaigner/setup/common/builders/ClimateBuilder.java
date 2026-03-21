package com.mcommings.campaigner.setup.common.builders;


import com.mcommings.campaigner.modules.common.entities.Climate;
import com.mcommings.campaigner.setup.common.fixtures.CommonTestConstants;

public class ClimateBuilder {

    private int id = CommonTestConstants.CLIMATE_ID;
    private String name = CommonTestConstants.CLIMATE_NAME;
    private String description = CommonTestConstants.CLIMATE_DESCRIPTION;

    public static ClimateBuilder aClimate() {
        return new ClimateBuilder();
    }

    public ClimateBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ClimateBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ClimateBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Climate build() {
        Climate climate = new Climate();
        climate.setId(id);
        climate.setName(name);
        climate.setDescription(description);
        return climate;
    }
}
