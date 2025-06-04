import 'dart:convert';

import 'package:frontend/models/location/settlementType.dart';
import 'package:http/http.dart' as http;

Future<List<SettlementType>> fetchSettlementTypes(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/settlementtypes/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => SettlementType.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load settlement types');
  }
}

Future<SettlementType> fetchSettlementType(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/settlementtypes/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return SettlementType.fromJson(data);
  } else {
    throw Exception('Failed to load settlement with id $id');
  }
}

Future<bool> createSettlementType(
  String uuid,
  String name,
  String description,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/settlements'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editSettlementType(
  String uuid,
  int id,
  String name,
  String description,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/settlementtypes/$id'),
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

Future<void> deleteSettlementType(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/settlementtypes/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    throw Exception('Failed to delete settlement types');
  }
}
