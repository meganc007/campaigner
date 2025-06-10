import 'dart:convert';

import 'package:frontend/models/location/continent.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Continent>> fetchContinents(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/continents/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Continent.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load continents');
  }
}

Future<Continent> fetchContinent(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/continents/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Continent.fromJson(data);
  } else {
    throw Exception('Failed to load continent with id $id');
  }
}

Future<bool> createContinent(
  String uuid,
  String name,
  String description,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/continents'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editContinent(
  String uuid,
  int id,
  String name,
  String description,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/continents/$id'),
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

Future<void> deleteContinent(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/continents/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("continent")
        : throw Exception('Failed to delete continent');
  }
}
