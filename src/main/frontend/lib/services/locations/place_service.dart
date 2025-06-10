import 'dart:convert';

import 'package:frontend/models/location/place.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Place>> fetchPlaces(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/places/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Place.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load places');
  }
}

Future<Place> fetchPlace(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/places/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Place.fromJson(data);
  } else {
    throw Exception('Failed to load place with id $id');
  }
}

Future<bool> createPlace(
  String uuid,
  String name,
  String description,
  int fkPlaceType,
  int? fkTerrain,
  int fkCountry,
  int? fkCity,
  int fkRegion,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/places'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_place_type": fkPlaceType,
      "fk_terrain": fkTerrain,
      "fk_country": fkCountry,
      "fk_city": fkCity,
      "fk_region": fkRegion,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editPlace(
  String uuid,
  int id,
  String name,
  String description,
  int fkPlaceType,
  int? fkTerrain,
  int fkCountry,
  int? fkCity,
  int fkRegion,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/places/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_place_type": fkPlaceType,
      "fk_terrain": fkTerrain,
      "fk_country": fkCountry,
      "fk_city": fkCity,
      "fk_region": fkRegion,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deletePlace(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/places/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("place")
        : throw Exception('Failed to delete place');
  }
}
