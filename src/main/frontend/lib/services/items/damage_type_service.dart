import 'dart:convert';

import 'package:frontend/models/items/damage_type.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<DamageType>> fetchDamageTypes() async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/damagetypes'),
  );
  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => DamageType.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load damage types');
  }
}

Future<DamageType> fetchDamageType(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/damagetypes/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return DamageType.fromJson(data);
  } else {
    throw Exception('Failed to load damage type with id $id');
  }
}

Future<bool> createDamageType(String name, String description) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/damagetypes'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editDamageType(int id, String name, String description) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/damagetypes/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"id": id, "name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteDamageType(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/damagetypes/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("damagetypes")
        : throw Exception('Failed to delete damage types');
  }
}
