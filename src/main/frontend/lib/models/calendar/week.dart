class Week {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;
  final int weekNumber;
  final int fkMonth;

  const Week({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
    required this.weekNumber,
    required this.fkMonth,
  });

  factory Week.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
        'week_number': int weekNumber,
        'fk_month': int fkMonth,
      } =>
        Week(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
          weekNumber: weekNumber,
          fkMonth: fkMonth,
        ),
      _ => throw const FormatException("Failed to load Week."),
    };
  }
}
