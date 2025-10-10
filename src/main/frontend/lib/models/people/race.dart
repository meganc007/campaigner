class Race {
  final int id;
  final String name;
  final String description;
  final bool isExotic;

  const Race({
    required this.id,
    required this.name,
    required this.description,
    required this.isExotic,
  });

  @override
  String toString() {
    return 'Race(id: $id, name: $name, description: $description, isExotic: $isExotic)';
  }

  factory Race.fromJson(Map<String, dynamic> json) {
    try {
      return Race(
        id: json['id'],
        name: json['name'],
        description: json['description'],
        isExotic: json['isExotic'],
      );
    } catch (e) {
      throw FormatException('Race parse error: $e');
    }
  }
}
