class Government {
  final int id;
  final String name;
  final String description;

  const Government({
    required this.id,
    required this.name,
    required this.description,
  });

  factory Government.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {'id': int id, 'name': String name, 'description': String description} =>
        Government(id: id, name: name, description: description),
      _ => throw const FormatException("Failed to load Government."),
    };
  }
}
