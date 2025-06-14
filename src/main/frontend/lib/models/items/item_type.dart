class ItemType {
  final int id;
  final String name;
  final String? description;

  const ItemType({required this.id, required this.name, this.description});

  factory ItemType.fromJson(Map<String, dynamic> json) {
    try {
      return ItemType(
        id: json['id'],
        name: json['name'],
        description: json['description'],
      );
    } catch (e) {
      throw FormatException('ItemType parse error: $e');
    }
  }
}
