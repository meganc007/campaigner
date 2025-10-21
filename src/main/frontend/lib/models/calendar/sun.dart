class Sun {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;

  const Sun({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
  });

  factory Sun.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
      } =>
        Sun(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
        ),
      _ => throw const FormatException("Failed to load Sun."),
    };
  }
}
