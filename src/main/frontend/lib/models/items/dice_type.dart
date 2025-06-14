class DiceType {
  final int id;
  final String name;
  final String description;
  final int maxRoll;

  const DiceType({
    required this.id,
    required this.name,
    required this.description,
    required this.maxRoll,
  });

  factory DiceType.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'id': int id,
        'name': String name,
        'description': String description,
        'max_roll': int maxRoll,
      } =>
        DiceType(
          id: id,
          name: name,
          description: description,
          maxRoll: maxRoll,
        ),
      _ => throw const FormatException("Failed to load DiceType."),
    };
  }
}
