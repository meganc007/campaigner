class Day {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;
  final int fkWeek;

  const Day({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
    required this.fkWeek,
  });

  factory Day.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
        'fk_week': int fkWeek,
      } =>
        Day(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
          fkWeek: fkWeek,
        ),
      _ => throw const FormatException("Failed to load Day."),
    };
  }
}
