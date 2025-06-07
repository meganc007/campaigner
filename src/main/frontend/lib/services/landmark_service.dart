import 'dart:convert';

import 'package:frontend/models/location/landmark.dart';
import 'package:http/http.dart' as http;

Future<List<Landmark>> fetchLandmarks(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/landmarks/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Landmark.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load landmarks');
  }
}

Future<Landmark> fetchLandmark(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/landmarks/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Landmark.fromJson(data);
  } else {
    throw Exception('Failed to load landmark with id $id');
  }
}

Future<bool> createLandmark(
  String uuid,
  String name,
  String description,
  int region,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/landmarks'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_region": region,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editLandmark(
  String uuid,
  int id,
  String name,
  String description,
  int region,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/landmarks/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_region": region,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteLandmark(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/landmarks/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    throw Exception('Failed to delete landmark');
  }
}
