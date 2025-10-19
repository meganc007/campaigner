class Wealth {
  final int id;
  final String name;

  const Wealth({required this.id, required this.name});

  factory Wealth.fromJson(Map<String, dynamic> json) {
    return switch (json) {
      {'id': int id, 'name': String name} => Wealth(id: id, name: name),
      _ => throw const FormatException("Failed to load Wealth."),
    };
  }
}
