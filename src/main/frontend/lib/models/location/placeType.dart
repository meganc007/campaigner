class PlaceType {
  final int id;
  final String name;
  final String description;

  const PlaceType({
    required this.id,
    required this.name,
    required this.description,
  });

  factory PlaceType.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {'id': int id, 'name': String name, 'description': String description} =>
        PlaceType(id: id, name: name, description: description),
      _ => throw const FormatException("Failed to load PlaceType."),
    };
  }
}
