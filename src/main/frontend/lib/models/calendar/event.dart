class Event {
  final int id;
  final String name;
  final String description;
  final int eventYear;
  final int? fkMonth;
  final int? fkWeek;
  final int? fkDay;
  final int? fkContinent;
  final int? fkCountry;
  final int? fkCity;
  final String fkCampaignUuid;

  const Event({
    required this.id,
    required this.name,
    required this.description,
    required this.eventYear,
    this.fkMonth,
    this.fkWeek,
    this.fkDay,
    this.fkContinent,
    this.fkCountry,
    this.fkCity,
    required this.fkCampaignUuid,
  });

  factory Event.fromJson(Map<String, dynamic> json) {
    try {
      return Event(
        id: json['id'] as int,
        name: json['name'] as String,
        description: json['description'] as String,
        // backend uses snake_case for event year in some places; accept either
        eventYear: (json['event_year'] ?? json['eventYear']) as int,
        fkMonth: json['fk_month'] as int?,
        fkWeek: json['fk_week'] as int?,
        fkDay: json['fk_day'] as int?,
        fkContinent: json['fk_continent'] as int?,
        fkCountry: json['fk_country'] as int?,
        fkCity: json['fk_city'] as int?,
        fkCampaignUuid: json['fk_campaign_uuid'] as String,
      );
    } catch (e) {
      throw FormatException('Event parse error: $e');
    }
  }
}
