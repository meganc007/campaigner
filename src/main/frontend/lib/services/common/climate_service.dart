import 'dart:convert';

import 'package:frontend/models/common/climate.dart';
import 'package:frontend/services/api.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Climate>> fetchClimates() async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/climates'));

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Climate.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load climates');
  }
}

Future<Climate> fetchClimate(int id) async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/climates/$id'));

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Climate.fromJson(data);
  } else {
    throw Exception('Failed to load climate with id $id');
  }
}

Future<bool> createClimate(String name, String description) async {
  final response = await http.post(
    Uri.parse('${Api.baseUrl}/climates'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editClimate(int id, String name, String description) async {
  final response = await http.put(
    Uri.parse('${Api.baseUrl}/climates/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"id": id, "name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteClimate(int id) async {
  final response = await http.delete(Uri.parse('${Api.baseUrl}/climates/$id'));

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("climate")
        : throw Exception('Failed to delete climate');
  }
}
