class WeaponType {
  final int id;
  final String name;
  final String? description;

  const WeaponType({
    required this.id,
    required this.name,
    required this.description,
  });

  factory WeaponType.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {'id': int id, 'name': String name, 'description': String description} =>
        WeaponType(id: id, name: name, description: description),
      _ => throw const FormatException("Failed to load WeaponType."),
    };
  }
}
