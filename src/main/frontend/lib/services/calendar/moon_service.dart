import 'dart:convert';

import 'package:frontend/models/calendar/moon.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Moon>> fetchMoons(String uuid) async {
  final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/moons'));

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Moon.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load moons');
  }
}

Future<Moon> fetchMoon(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/moons/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Moon.fromJson(data);
  } else {
    throw Exception('Failed to load moon with id $id');
  }
}

Future<bool> createMoon(String name, String description, String uuid) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/moons'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editMoon(
  int id,
  String name,
  String description,
  String uuid,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/moons/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteMoon(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/moons/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("moon")
        : throw Exception('Failed to delete moon');
  }
}
