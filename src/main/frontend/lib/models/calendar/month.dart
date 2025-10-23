class Month {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;
  final String season;

  const Month({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
    required this.season,
  });

  factory Month.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
        'season': String season,
      } =>
        Month(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
          season: season,
        ),
      _ => throw const FormatException("Failed to load Month."),
    };
  }
}
