import 'dart:convert';

import 'package:frontend/models/location/region.dart';
import 'package:frontend/services/api.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Region>> fetchRegions(String uuid) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/regions/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Region.fromJson((json))).toList();
  } else {
    throw Exception('Failed to load regions');
  }
}

Future<Region> fetchRegion(int id) async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/regions/$id'));

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Region.fromJson(data);
  } else {
    throw Exception('Failed to load region with id $id');
  }
}

Future<bool> createRegion(
  String uuid,
  String name,
  String description,
  int country,
  int climate,
) async {
  final response = await http.post(
    Uri.parse('${Api.baseUrl}/regions'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_country": country,
      "fk_climate": climate,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editRegion(
  String uuid,
  int id,
  String name,
  String description,
  int country,
  int climate,
) async {
  final response = await http.put(
    Uri.parse('${Api.baseUrl}/regions/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_country": country,
      "fk_climate": climate,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteRegion(int id) async {
  final response = await http.delete(Uri.parse('${Api.baseUrl}/regions/$id'));

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("region")
        : throw Exception('Failed to delete region');
  }
}
