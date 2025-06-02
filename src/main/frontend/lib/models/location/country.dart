class Country {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;
  final int fkContinent;
  final int fkGovernment;

  const Country({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
    required this.fkContinent,
    required this.fkGovernment,
  });

  factory Country.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
        'fk_continent': int fkContinent,
        'fk_government': int fkGovernment,
      } =>
        Country(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
          fkContinent: fkContinent,
          fkGovernment: fkGovernment,
        ),
      _ => throw const FormatException("Failed to load Country."),
    };
  }
}
