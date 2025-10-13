import 'dart:convert';

import 'package:frontend/models/people/race.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Race>> fetchRaces() async {
  final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/races'));

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Race.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load races');
  }
}

Future<Race> fetchRace(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/races/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Race.fromJson(data);
  } else {
    throw Exception('Failed to load race with id $id');
  }
}

Future<bool> createRace(String name, String description, bool? isExotic) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/races'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "isExotic": isExotic,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editRace(
  int id,
  String name,
  String description,
  bool? isExotic,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/races/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "isExotic": isExotic,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteRace(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/races/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("race")
        : throw Exception('Failed to delete race');
  }
}
