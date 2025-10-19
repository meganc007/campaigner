class AbilityScore {
  final int id;
  final String fkCampaignUuid;
  final int strength;
  final int dexterity;
  final int constitution;
  final int intelligence;
  final int wisdom;
  final int charisma;

  const AbilityScore({
    required this.id,
    required this.fkCampaignUuid,
    required this.strength,
    required this.dexterity,
    required this.constitution,
    required this.intelligence,
    required this.wisdom,
    required this.charisma,
  });

  @override
  String toString() {
    return 'AbilityScore(id: $id, fkCampaignUuid: $fkCampaignUuid, strength: $strength, dexterity: $dexterity, constitution: $constitution, intelligence: $intelligence, wisdom: $wisdom, charisma: $charisma)';
  }

  String toShortString() {
    return 'id: $id,\n strength: $strength,\n dexterity: $dexterity,\n constitution: $constitution,\n intelligence: $intelligence,\n wisdom: $wisdom,\n charisma: $charisma';
  }

  String statsOnly() {
    return 'strength: $strength,\n dexterity: $dexterity,\n constitution: $constitution,\n intelligence: $intelligence,\n wisdom: $wisdom,\n charisma: $charisma';
  }

  factory AbilityScore.fromJson(Map<String, dynamic> json) {
    try {
      return AbilityScore(
        id: json['id'],
        fkCampaignUuid: json['fk_campaign_uuid'],
        strength: json['strength'],
        dexterity: json['dexterity'],
        constitution: json['constitution'],
        intelligence: json['intelligence'],
        wisdom: json['wisdom'],
        charisma: json['charisma'],
      );
    } catch (e) {
      throw FormatException('AbilityScore parse error: $e');
    }
  }
}
