class DamageType {
  final int id;
  final String name;
  final String? description;

  const DamageType({
    required this.id,
    required this.name,
    required this.description,
  });

  factory DamageType.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {'id': int id, 'name': String name, 'description': String description} =>
        DamageType(id: id, name: name, description: description),
      _ => throw const FormatException("Failed to load DamageType."),
    };
  }
}
