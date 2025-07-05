class Inventory {
  final int id;
  final String fkCampaignUuid;
  final int? fkPerson;
  final int? fkItem;
  final int? fkWeapon;
  final int? fkPlace;
  final String? personName;
  final String? placeName;

  const Inventory({
    required this.id,
    required this.fkCampaignUuid,
    required this.fkPerson,
    required this.fkItem,
    required this.fkWeapon,
    required this.fkPlace,
    this.personName,
    this.placeName,
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
        personName: json['personName'],
        placeName: json['placeName'],
      );
    } catch (e) {
      throw FormatException('Inventory parse error: $e');
    }
  }
}
