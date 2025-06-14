class Weapon {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;
  final String rarity;
  final int goldValue;
  final int silverValue;
  final int copperValue;
  final double weight;
  final int fkWeaponType;
  final int fkDamageType;
  final int fkDiceType;
  final int numberOfDice;
  final int damageModifier;
  final bool isMagical;
  final bool isCursed;
  final String notes;

  const Weapon({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
    required this.rarity,
    required this.goldValue,
    required this.silverValue,
    required this.copperValue,
    required this.weight,
    required this.fkWeaponType,
    required this.fkDamageType,
    required this.fkDiceType,
    required this.numberOfDice,
    required this.damageModifier,
    required this.isMagical,
    required this.isCursed,
    required this.notes,
  });

  factory Weapon.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
        'rarity': String rarity,
        'gold_value': int goldValue,
        'silver_value': int silverValue,
        'copper_value': int copperValue,
        'weight': double weight,
        'fk_weapon_type': int fkWeaponType,
        'fk_damage_type': int fkDamageType,
        'fk_dice_type': int fkDiceType,
        'number_of_dice': int numberOfDice,
        'damage_modifier': int damageModifier,
        'isMagical': bool isMagical,
        'isCursed': bool isCursed,
        'notes': String notes,
      } =>
        Weapon(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
          rarity: rarity,
          goldValue: goldValue,
          silverValue: silverValue,
          copperValue: copperValue,
          weight: weight,
          fkWeaponType: fkWeaponType,
          fkDamageType: fkDamageType,
          fkDiceType: fkDiceType,
          numberOfDice: numberOfDice,
          damageModifier: damageModifier,
          isMagical: isMagical,
          isCursed: isCursed,
          notes: notes,
        ),
      _ => throw const FormatException("Failed to load Weapon."),
    };
  }
}
