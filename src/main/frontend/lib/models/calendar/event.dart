class Event {
  final int id;
  final String name;
  final String description;
  final int eventYear;
  final int fkMonth;
  final int fkWeek;
  final int fkDay;
  final int fkContinent;
  final int fkCountry;
  final int fkCity;
  final String fkCampaignUuid;

  const Event({
    required this.id,
    required this.name,
    required this.description,
    required this.eventYear,
    required this.fkMonth,
    required this.fkWeek,
    required this.fkDay,
    required this.fkContinent,
    required this.fkCountry,
    required this.fkCity,
    required this.fkCampaignUuid,
  });

  factory Event.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'event_year': int eventYear,
        'fk_month': int fkMonth,
        'fk_week': int fkWeek,
        'fk_day': int fkDay,
        'fk_continent': int fkContinent,
        'fk_country': int fkCountry,
        'fk_city': int fkCity,
        'fk_campaign_uuid': String fkCampaignUuid,
      } =>
        Event(
          id: id,
          name: name,
          description: description,
          eventYear: eventYear,
          fkMonth: fkMonth,
          fkWeek: fkWeek,
          fkDay: fkDay,
          fkContinent: fkContinent,
          fkCountry: fkCountry,
          fkCity: fkCity,
          fkCampaignUuid: fkCampaignUuid,
        ),
      _ => throw const FormatException("Failed to load Event."),
    };
  }
}
