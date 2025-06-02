class City {
  final int id;
  final String name;
  final String description;
  final String fkCampaignUuid;
  final int fkWealth;
  final int fkCountry;
  final int fkSettlement;
  final int fkGovernment;
  final int fkRegion;

  const City({
    required this.id,
    required this.name,
    required this.description,
    required this.fkCampaignUuid,
    required this.fkWealth,
    required this.fkCountry,
    required this.fkSettlement,
    required this.fkGovernment,
    required this.fkRegion,
  });

  factory City.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'fk_campaign_uuid': String fkCampaignUuid,
        'fk_wealth': int fkWealth,
        'fk_country': int fkCountry,
        'fk_settlement': int fkSettlement,
        'fk_government': int fkGovernment,
        'fk_region': int fkRegion,
      } =>
        City(
          id: id,
          name: name,
          description: description,
          fkCampaignUuid: fkCampaignUuid,
          fkWealth: fkWealth,
          fkCountry: fkCountry,
          fkSettlement: fkSettlement,
          fkGovernment: fkGovernment,
          fkRegion: fkRegion,
        ),
      _ => throw const FormatException("Failed to load City."),
    };
  }
}
