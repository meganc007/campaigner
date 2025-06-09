import 'dart:convert';

import 'package:frontend/models/campaign.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Campaign>> fetchCampaigns() async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/campaigns'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Campaign.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load campaigns');
  }
}

Future<Campaign> fetchCampaign(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/campaigns/$uuid'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Campaign.fromJson(data);
  } else {
    throw Exception('Failed to load campaign with uuid $uuid');
  }
}

Future<bool> createCampaign(String name, String description) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/campaigns'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editCampaign(String uuid, String name, String description) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/campaigns/$uuid'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"uuid": uuid, "name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteCampaign(String uuid) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/campaigns/$uuid'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("campaign")
        : throw Exception('Failed to delete campaign');
  }
}
