class CelestialEvent {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;
  final int fkMoon;
  final int fkSun;
  final int fkMonth;
  final int fkWeek;
  final int fkDay;
  final int eventYear;

  const CelestialEvent({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
    required this.fkMoon,
    required this.fkSun,
    required this.fkMonth,
    required this.fkWeek,
    required this.fkDay,
    required this.eventYear,
  });

  factory CelestialEvent.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
        'fk_moon': int fkMoon,
        'fk_sun': int fkSun,
        'fk_month': int fkMonth,
        'fk_week': int fkWeek,
        'fk_day': int fkDay,
        'event_year': int eventYear,
      } =>
        CelestialEvent(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
          fkMoon: fkMoon,
          fkSun: fkSun,
          fkMonth: fkMonth,
          fkWeek: fkWeek,
          fkDay: fkDay,
          eventYear: eventYear,
        ),
      _ => throw const FormatException("Failed to load CelestialEvent."),
    };
  }
}
