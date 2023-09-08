package com.mcommings.campaigner.enums;

public enum ForeignKey {

    FK_ABILITY_SCORE("fk_ability_score"),
    FK_CLIMATE("fk_climate"),
    FK_CITY("fk_city"),
    FK_CONTINENT("fk_continent"),
    FK_COUNTRY("fk_country"),
    FK_DAMAGE_TYPE("fk_damage_type"),
    FK_DICE_TYPE("fk_dice_type"),
    FK_DAY("fk_day"),
    FK_EVENT("fk_event"),
    FK_GENERIC_MONSTER("fk_generic_monster"),
    FK_GOVERNMENT("fk_government"),
    FK_HOOK("fk_hook"),
    FK_ITEM("fk_item"),
    FK_ITEM_TYPE("fk_item_type"),
    FK_JOB("fk_job"),
    FK_MONTH("fk_month"),
    FK_MOON("fk_moon"),
    FK_NAMED_MONSTER("fk_named_monster"),
    FK_OBJECTIVE("fk_objective"),
    FK_OUTCOME("fk_outcome"),
    FK_PERSON("fk_person"),
    FK_PLACE("fk_place"),
    FK_PLACE_TYPE("fk_place_type"),
    FK_RACE("fk_race"),
    FK_REWARD("fk_reward"),
    FK_REGION("fk_region"),
    FK_SETTLEMENT("fk_settlement"),
    FK_SUN("fk_sun"),
    FK_TERRAIN("fk_terrain"),
    FK_WEALTH("fk_wealth"),
    FK_WEAPON("fk_weapon"),
    FK_WEAPON_TYPE("fk_weapon_type"),
    FK_WEEK("fk_week");

    public final String columnName;

    ForeignKey(String columnName) {
        this.columnName = columnName;
    }
}
