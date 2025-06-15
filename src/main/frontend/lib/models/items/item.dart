class Item {
  final int id;
  final String name;
  final String? description;
  final String fkCampaignUuid;
  final String? rarity;
  final int? goldValue;
  final int? silverValue;
  final int? copperValue;
  final double? weight;
  final int? fkItemType;
  final bool? isMagical;
  final bool? isCursed;
  final String? notes;

  const Item({
    required this.id,
    required this.name,
    this.description,
    required this.fkCampaignUuid,
    this.rarity,
    this.goldValue,
    this.silverValue,
    this.copperValue,
    this.weight,
    this.fkItemType,
    this.isMagical,
    this.isCursed,
    this.notes,
  });

  factory Item.fromJson(Map<String, dynamic> json) {
    try {
      return Item(
        id: json['id'],
        name: json['name'],
        description: json['description'],
        fkCampaignUuid: json['fk_campaign_uuid'],
        rarity: json['rarity'],
        goldValue: json['gold_value'],
        silverValue: json['silver_value'],
        copperValue: json['copper_value'],
        weight: json['weight'],
        fkItemType: json['fk_item_type'],
        isMagical: json['isMagical'] == null ? null : json['isMagical'] as bool,
        isCursed: json['isCursed'] == null ? null : json['isCursed'] as bool,
        notes: json['notes'],
      );
    } catch (e) {
      throw FormatException('Item parse error: $e');
    }
  }
}
