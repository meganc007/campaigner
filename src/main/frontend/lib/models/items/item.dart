class Item {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;
  final String rarity;
  final int goldValue;
  final int silverValue;
  final int copperValue;
  final double weight;
  final int fkItemType;
  final bool isMagical;
  final bool isCursed;
  final String notes;

  const Item({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
    required this.rarity,
    required this.goldValue,
    required this.silverValue,
    required this.copperValue,
    required this.weight,
    required this.fkItemType,
    required this.isMagical,
    required this.isCursed,
    required this.notes,
  });

  factory Item.fromJson(Map<String, dynamic> json) {
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
        'fk_item_type': int fkItemType,
        'isMagical': bool isMagical,
        'isCursed': bool isCursed,
        'notes': String notes,
      } =>
        Item(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
          rarity: rarity,
          goldValue: goldValue,
          silverValue: silverValue,
          copperValue: copperValue,
          weight: weight,
          fkItemType: fkItemType,
          isMagical: isMagical,
          isCursed: isCursed,
          notes: notes,
        ),
      _ => throw const FormatException("Failed to load Item."),
    };
  }
}
