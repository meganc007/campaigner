class Continent {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;

  const Continent({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
  });

  factory Continent.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String campaignUuid,
      } =>
        Continent(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: campaignUuid,
        ),
      _ => throw const FormatException("Failed to load Continent."),
    };
  }
}
