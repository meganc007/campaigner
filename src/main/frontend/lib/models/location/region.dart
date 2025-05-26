class Region {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;
  final int fkCountry;
  final int fkClimate;

  const Region({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
    required this.fkCountry,
    required this.fkClimate,
  });

  factory Region.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
        'fk_country': int fkCountry,
        'fk_climate': int fkClimate,
      } =>
        Region(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
          fkCountry: fkCountry,
          fkClimate: fkClimate,
        ),
      _ => throw const FormatException("Failed to load Region."),
    };
  }
}
