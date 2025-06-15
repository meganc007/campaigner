class Inventory {
  final int id;
  final String fkCampaignUuid;
  final int? fkPerson;
  final int? fkItem;
  final int? fkWeapon;
  final int? fkPlace;

  const Inventory({
    required this.id,
    required this.fkCampaignUuid,
    required this.fkPerson,
    required this.fkItem,
    required this.fkWeapon,
    required this.fkPlace,
  });

  factory Inventory.fromJson(Map<String, dynamic> json) {
    try {
      return Inventory(
        id: json['id'],
        fkCampaignUuid: json['fk_campaign_uuid'],
        fkPerson: json['fk_person'],
        fkItem: json['fk_item'],
        fkWeapon: json['fk_weapon'],
        fkPlace: json['fk_place'],
      );
    } catch (e) {
      throw FormatException('Inventory parse error: $e');
    }
  }
}
