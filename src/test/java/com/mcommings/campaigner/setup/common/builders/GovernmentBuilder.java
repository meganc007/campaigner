package com.mcommings.campaigner.setup.common.builders;

import com.mcommings.campaigner.modules.common.entities.Government;
import com.mcommings.campaigner.setup.common.fixtures.CommonTestConstants;

public class GovernmentBuilder {

    private int id = CommonTestConstants.GOVERNMENT_ID;
    private String name = CommonTestConstants.GOVERNMENT_NAME;
    private String description = CommonTestConstants.GOVERNMENT_DESCRIPTION;

    public static GovernmentBuilder aGovernment() {
        return new GovernmentBuilder();
    }

    public GovernmentBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public GovernmentBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public GovernmentBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Government build() {
        Government government = new Government();
        government.setId(id);
        government.setName(name);
        government.setDescription(description);
        return government;
    }
}
