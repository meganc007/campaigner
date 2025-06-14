class Inventory {
  final int id;
  final String fkCampaignUuid;
  final int fkPerson;
  final int fkItem;
  final int fkWeapon;
  final int fkPlace;

  const Inventory({
    required this.id,
    required this.fkCampaignUuid,
    required this.fkPerson,
    required this.fkItem,
    required this.fkWeapon,
    required this.fkPlace,
  });

  factory Inventory.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'fk_campaign_uuid': String fkCampaignUuid,
        'fk_person': int fkPerson,
        'fk_item': int fkItem,
        'fk_weapon': int fkWeapon,
        'fk_place': int fkPlace,
      } =>
        Inventory(
          id: id,
          fkCampaignUuid: fkCampaignUuid,
          fkPerson: fkPerson,
          fkItem: fkItem,
          fkWeapon: fkWeapon,
          fkPlace: fkPlace,
        ),
      _ => throw const FormatException("Failed to load Inventory."),
    };
  }
}
