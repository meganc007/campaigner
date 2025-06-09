import 'dart:convert';

import 'package:frontend/models/location/terrain.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Terrain>> fetchTerrains() async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/terrains'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Terrain.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load terrains');
  }
}

Future<Terrain> fetchTerrain(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/terrains/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Terrain.fromJson(data);
  } else {
    throw Exception('Failed to load terrain with id $id');
  }
}

Future<bool> createTerrain(String name, String description) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/terrains'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editTerrain(int id, String name, String description) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/terrains/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"id": id, "name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteTerrain(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/terrains/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("terrain")
        : throw Exception('Failed to delete terrain');
  }
}
