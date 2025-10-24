import 'dart:convert';

import 'package:frontend/models/items/weapon_type.dart';
import 'package:frontend/services/api.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<WeaponType>> fetchWeaponTypes() async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/weapontypes'));
  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => WeaponType.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load weapon types');
  }
}

Future<WeaponType> fetchWeaponType(int id) async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/weapontypes/$id'));

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return WeaponType.fromJson(data);
  } else {
    throw Exception('Failed to load weapon types with id $id');
  }
}

Future<bool> createWeaponType(String name, String description) async {
  final response = await http.post(
    Uri.parse('${Api.baseUrl}/weapontypes'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editWeaponType(int id, String name, String description) async {
  final response = await http.put(
    Uri.parse('${Api.baseUrl}/weapontypes/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"id": id, "name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteWeaponType(int id) async {
  final response = await http.delete(
    Uri.parse('${Api.baseUrl}/weapontypes/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("weapontypes")
        : throw Exception('Failed to delete weapon types');
  }
}
