import 'dart:convert';

import 'package:frontend/models/location/place_type.dart';
import 'package:frontend/services/api.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<PlaceType>> fetchPlaceTypes() async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/placetypes'));

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => PlaceType.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load place types');
  }
}

Future<PlaceType> fetchPlaceType(int id) async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/placetypes/$id'));

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return PlaceType.fromJson(data);
  } else {
    throw Exception('Failed to load place type with id $id');
  }
}

Future<bool> createPlaceType(String name, String description) async {
  final response = await http.post(
    Uri.parse('${Api.baseUrl}/placetypes'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editPlaceType(int id, String name, String description) async {
  final response = await http.put(
    Uri.parse('${Api.baseUrl}/placetypes/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"id": id, "name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deletePlaceType(int id) async {
  final response = await http.delete(
    Uri.parse('${Api.baseUrl}/placetypes/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("place type")
        : throw Exception('Failed to delete place type');
  }
}
