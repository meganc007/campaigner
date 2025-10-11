class NamedMonster {
  final int id;
  final String firstName;
  final String? lastName;
  final String title;
  final int? fkWealth;
  final int? fkAbilityScore;
  final int? fkGenericMonster;
  final bool? isEnemy;
  final String personality;
  final String description;
  final String notes;
  final String fkCampaignUuid;

  const NamedMonster({
    required this.id,
    required this.firstName,
    this.lastName,
    required this.title,
    this.fkWealth,
    this.fkAbilityScore,
    this.fkGenericMonster,
    this.isEnemy,
    required this.personality,
    required this.description,
    required this.notes,
    required this.fkCampaignUuid,
  });

  String get fullName {
    final f = firstName.trim();
    final l = (lastName ?? '').trim();
    if (l.isEmpty) return f;
    return '$f $l';
  }

  String get name => fullName;

  factory NamedMonster.fromJson(Map<String, dynamic> json) {
    try {
      return NamedMonster(
        id: json['id'],
        firstName: json['firstName'],
        lastName: json['lastName'],
        title: json['title'],
        fkWealth: json['fk_wealth'],
        fkAbilityScore: json['fk_ability_score'],
        fkGenericMonster: json['fk_generic_monster'],
        isEnemy: json['isEnemy'] ?? false,
        personality: json['personality'],
        description: json['description'],
        notes: json['notes'],
        fkCampaignUuid: json['fk_campaign_uuid'],
      );
    } catch (e) {
      throw FormatException('NamedMonster parse error: $e');
    }
  }
}
