class Landmark {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;
  final int fkRegion;

  const Landmark({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
    required this.fkRegion,
  });

  factory Landmark.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
        'fk_region': int fkRegion,
      } =>
        Landmark(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
          fkRegion: fkRegion,
        ),
      _ => throw const FormatException("Failed to load Landmark."),
    };
  }
}
