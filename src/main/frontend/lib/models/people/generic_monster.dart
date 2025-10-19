class GenericMonster {
  final int id;
  final String name;
  final String? description;
  final int? fkAbilityScore;
  final String? traits;
  final String? notes;
  final String fkCampaignUuid;

  const GenericMonster({
    required this.id,
    required this.name,
    this.description,
    this.fkAbilityScore,
    this.traits,
    this.notes,
    required this.fkCampaignUuid,
  });

  @override
  String toString() {
    return 'GenericMonster(id: $id, name: $name, description: $description, fkAbilityScore: $fkAbilityScore, traits: $traits, notes: $notes, fkCampaignUuid: $fkCampaignUuid)';
  }

  factory GenericMonster.fromJson(Map<String, dynamic> json) {
    try {
      return GenericMonster(
        id: json['id'],
        name: json['name'],
        description: json['description'],
        fkAbilityScore: json['fk_ability_score'],
        traits: json['traits'],
        notes: json['notes'],
        fkCampaignUuid: json['fk_campaign_uuid'],
      );
    } catch (e) {
      throw FormatException('GenericMonster parse error: $e');
    }
  }
}
