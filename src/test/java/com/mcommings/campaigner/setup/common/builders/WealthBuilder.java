package com.mcommings.campaigner.setup.common.builders;

import com.mcommings.campaigner.modules.common.entities.Wealth;
import com.mcommings.campaigner.setup.common.fixtures.CommonTestConstants;

public class WealthBuilder {

    private int id = CommonTestConstants.WEALTH_ID;
    private String name = CommonTestConstants.WEALTH_NAME;

    public static WealthBuilder aWealth() {
        return new WealthBuilder();
    }

    public WealthBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public WealthBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Wealth build() {
        Wealth wealth = new Wealth();
        wealth.setId(id);
        wealth.setName(name);
        return wealth;
    }
}
