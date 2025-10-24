import 'dart:convert';

import 'package:frontend/models/calendar/sun.dart';
import 'package:frontend/services/api.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Sun>> fetchSuns(String uuid) async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/suns'));

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Sun.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load suns');
  }
}

Future<Sun> fetchSun(int id) async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/suns/$id'));

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Sun.fromJson(data);
  } else {
    throw Exception('Failed to load sun with id $id');
  }
}

Future<bool> createSun(String name, String description, String uuid) async {
  final response = await http.post(
    Uri.parse('${Api.baseUrl}/suns'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editSun(
  int id,
  String name,
  String description,
  String uuid,
) async {
  final response = await http.put(
    Uri.parse('${Api.baseUrl}/suns/$id'),
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

Future<void> deleteSun(int id) async {
  final response = await http.delete(Uri.parse('${Api.baseUrl}/suns/$id'));

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("sun")
        : throw Exception('Failed to delete sun');
  }
}
