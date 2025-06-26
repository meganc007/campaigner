class Weapon {
  final int id;
  final String name;
  final String? description;
  final String fkCampaignUuid;
  final String? rarity;
  final int? goldValue;
  final int? silverValue;
  final int? copperValue;
  final double? weight;
  final int? fkWeaponType;
  final int? fkDamageType;
  final int? fkDiceType;
  final int? numberOfDice;
  final int? damageModifier;
  final bool? isMagical;
  final bool? isCursed;
  final String? notes;

  const Weapon({
    required this.id,
    required this.name,
    this.description,
    required this.fkCampaignUuid,
    this.rarity,
    this.goldValue,
    this.silverValue,
    this.copperValue,
    this.weight,
    this.fkWeaponType,
    this.fkDamageType,
    this.fkDiceType,
    this.numberOfDice,
    this.damageModifier,
    this.isMagical,
    this.isCursed,
    this.notes,
  });

  factory Weapon.fromJson(Map<String, dynamic> json) {
    try {
      return Weapon(
        id: json['id'],
        name: json['name'],
        description: json['description'],
        fkCampaignUuid: json['fk_campaign_uuid'],
        rarity: json['rarity'],
        goldValue: json['gold_value'],
        silverValue: json['silver_value'],
        copperValue: json['copper_value'],
        weight: json['weight'] is int
            ? (json['weight'] as int).toDouble()
            : json['weight'] as double?,
        fkWeaponType: json['fk_weapon_type'],
        fkDamageType: json['fk_damage_type'],
        fkDiceType: json['fk_dice_type'],
        numberOfDice: json['number_of_dice'],
        damageModifier: json['damage_modifier'],
        isMagical: json['isMagical'] == null ? null : json['isMagical'] as bool,
        isCursed: json['isCursed'] == null ? null : json['isCursed'] as bool,
        notes: json['notes'],
      );
    } catch (e) {
      throw FormatException('Weapon parse error: $e');
    }
  }
}
