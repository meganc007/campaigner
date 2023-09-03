package com.mcommings.campaigner.enums;

public enum ForeignKey {

    FK_ABILITY_SCORE("fk_ability_score"),
    FK_CLIMATE("fk_climate"),
    FK_CITY("fk_city"),
    FK_CONTINENT("fk_continent"),
    FK_COUNTRY("fk_country"),
    FK_DAY("fk_day"),
    FK_EVENT("fk_event"),
    FK_GOVERNMENT("fk_government"),
    FK_MONTH("fk_month"),
    FK_MOON("fk_moon"),
    FK_PERSON("fk_person"),
    FK_PLACE_TYPE("fk_place_type"),
    FK_RACE("fk_race"),
    FK_REGION("fk_region"),
    FK_SETTLEMENT("fk_settlement"),
    FK_SUN("fk_sun"),
    FK_TERRAIN("fk_terrain"),
    FK_WEALTH("fk_wealth"),
    FK_WEEK("fk_week");

    public final String columnName;

    ForeignKey(String columnName) {
        this.columnName = columnName;
    }
}
