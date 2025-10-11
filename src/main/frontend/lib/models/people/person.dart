class Person {
  final int id;
  final String firstName;
  final String? lastName;
  final int? age;
  final String? title;
  final int? fkRace;
  final int? fkWealth;
  final int? fkAbilityScore;
  final bool isNpc;
  final bool isEnemy;
  final String? personality;
  final String? description;
  final String? notes;
  final String fkCampaignUuid;

  const Person({
    required this.id,
    required this.firstName,
    this.lastName,
    this.age,
    this.title,
    this.fkRace,
    this.fkWealth,
    this.fkAbilityScore,
    required this.isNpc,
    required this.isEnemy,
    this.personality,
    this.description,
    this.notes,
    required this.fkCampaignUuid,
  });

  String get fullName {
    final f = firstName.trim();
    final l = (lastName ?? '').trim();
    if (l.isEmpty) return f;
    return '$f $l';
  }

  String get name => fullName;

  factory Person.fromJson(Map<String, dynamic> json) {
    try {
      return Person(
        id: json['id'],
        firstName: json['firstName'],
        lastName: json['lastName'],
        age: json['age'],
        title: json['title'],
        fkRace: json['fk_race'],
        fkWealth: json['fk_wealth'],
        fkAbilityScore: json['fk_ability_score'],
        isNpc: json['isNpc'] ?? false,
        isEnemy: json['isEnemy'] ?? false,
        personality: json['personality'],
        description: json['description'],
        notes: json['notes'],
        fkCampaignUuid: json['fk_campaign_uuid'],
      );
    } catch (e) {
      throw FormatException('Person parse error: $e');
    }
  }
}
