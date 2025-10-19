class Campaign {
  final String uuid;
  final String name;
  final String description;

  const Campaign({
    required this.uuid,
    required this.name,
    required this.description,
  });

  factory Campaign.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {
        'uuid': String uuid,
        'name': String name,
        'description': String description,
      } =>
        Campaign(uuid: uuid, name: name, description: description),
      _ => throw const FormatException("Failed to load Campaign."),
    };
  }
}
