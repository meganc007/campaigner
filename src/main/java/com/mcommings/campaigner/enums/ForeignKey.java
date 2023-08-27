package com.mcommings.campaigner.enums;

public enum ForeignKey {

    FK_CONTINENT("fk_continent"),
    FK_COUNTRY("fk_country"),
    FK_GOVERNMENT("fk_government"),
    FK_SETTLEMENT("fk_settlement"),
    FK_WEALTH("fk_wealth");

    public final String columnName;

    ForeignKey(String columnName) {
        this.columnName = columnName;
    }
}
