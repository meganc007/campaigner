class Place {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;
  final int fkPlaceType;
  final int? fkTerrain;
  final int fkCountry;
  final int? fkCity;
  final int fkRegion;

  const Place({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
    required this.fkPlaceType,
    this.fkTerrain,
    required this.fkCountry,
    this.fkCity,
    required this.fkRegion,
  });

  factory Place.fromJson(Map<String, dynamic> json) {
    return Place(
      id: json['id'],
      name: json['name'],
      description: json['description'],
      fkCampaignUuid: json['fk_campaign_uuid'],
      fkPlaceType: json['fk_place_type'],
      fkTerrain: json['fk_terrain'],
      fkCountry: json['fk_country'],
      fkCity: json['fk_city'],
      fkRegion: json['fk_region'],
    );
  }
}
