class Moon {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;

  const Moon({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
  });

  factory Moon.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
      } =>
        Moon(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
        ),
      _ => throw const FormatException("Failed to load Moon."),
    };
  }
}
