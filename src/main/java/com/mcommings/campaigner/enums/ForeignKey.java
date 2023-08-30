package com.mcommings.campaigner.enums;

public enum ForeignKey {

    FK_CITY("fk_city"),
    FK_CONTINENT("fk_continent"),
    FK_COUNTRY("fk_country"),
    FK_GOVERNMENT("fk_government"),
    FK_MONTH("fk_month"),
    FK_PLACE_TYPE("fk_place_type"),
    FK_SETTLEMENT("fk_settlement"),
    FK_TERRAIN("fk_terrain"),
    FK_WEALTH("fk_wealth"),
    FK_WEEK("fk_week");

    public final String columnName;

    ForeignKey(String columnName) {
        this.columnName = columnName;
    }
}
