import 'dart:convert';

import 'package:frontend/models/location/country.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Country>> fetchCountries(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/countries/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Country.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load countries');
  }
}

Future<Country> fetchCountry(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/countries/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Country.fromJson(data);
  } else {
    throw Exception('Failed to load country with id $id');
  }
}

Future<bool> createCountry(
  String uuid,
  String name,
  String description,
  int continent,
  int government,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/countries'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_continent": continent,
      "fk_government": government,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editCountry(
  String uuid,
  int id,
  String name,
  String description,
  int continent,
  int government,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/countries/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_continent": continent,
      "fk_government": government,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteCountry(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/countries/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("country")
        : throw Exception('Failed to delete country');
  }
}
