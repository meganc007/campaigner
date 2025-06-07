import 'dart:convert';

import 'package:frontend/models/location/city.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<City>> fetchCities(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/cities/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => City.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load cities');
  }
}

Future<City> fetchCity(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/cities/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return City.fromJson(data);
  } else {
    throw Exception('Failed to load city with id $id');
  }
}

Future<bool> createCity(
  String uuid,
  String name,
  String description,
  int fkWealth,
  int fkCountry,
  int fkSettlement,
  int fkGovernment,
  int fkRegion,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/cities'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_wealth": fkWealth,
      "fk_country": fkCountry,
      "fk_settlement": fkSettlement,
      "fk_government": fkGovernment,
      "fk_region": fkRegion,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editCity(
  String uuid,
  int id,
  String name,
  String description,
  int fkWealth,
  int fkCountry,
  int fkSettlement,
  int fkGovernment,
  int fkRegion,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/cities/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_wealth": fkWealth,
      "fk_country": fkCountry,
      "fk_settlement": fkSettlement,
      "fk_government": fkGovernment,
      "fk_region": fkRegion,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteCity(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/cities/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("city")
        : throw Exception('Failed to delete city');
  }
}
