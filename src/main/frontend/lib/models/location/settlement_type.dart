class SettlementType {
  final int id;
  final String name;
  final String description;

  const SettlementType({
    required this.id,
    required this.name,
    required this.description,
  });

  factory SettlementType.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {'id': int id, 'name': String name, 'description': String description} =>
        SettlementType(id: id, name: name, description: description),
      _ => throw const FormatException("Failed to load Settlement Type."),
    };
  }
}
