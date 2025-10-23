class Week {
  final int id;
  final String description;
  final String fkCampaignUuid;
  final int weekNumber;
  final int fkMonth;

  const Week({
    required this.id,
    required this.description,
    required this.fkCampaignUuid,
    required this.weekNumber,
    required this.fkMonth,
  });

  factory Week.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
        'week_number': int weekNumber,
        'fk_month': int fkMonth,
      } =>
        Week(
          id: id,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
          weekNumber: weekNumber,
          fkMonth: fkMonth,
        ),
      _ => throw FormatException("Failed to load Week."),
    };
  }
}
