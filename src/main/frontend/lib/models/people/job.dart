class Job {
  final int id;
  final String name;
  final String description;

  const Job({required this.id, required this.name, required this.description});

  @override
  String toString() {
    return 'Job(id: $id, name: $name, description: $description)';
  }

  factory Job.fromJson(Map<String, dynamic> json) {
    try {
      return Job(
        id: json['id'],
        name: json['name'],
        description: json['description'],
      );
    } catch (e) {
      throw FormatException('Job parse error: $e');
    }
  }
}
