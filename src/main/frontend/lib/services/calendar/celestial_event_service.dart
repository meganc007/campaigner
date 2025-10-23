import 'dart:convert';

import 'package:frontend/models/calendar/celestial_event.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<CelestialEvent>> fetchCelestialEvents(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/celestialevents'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => CelestialEvent.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load celestialEvents');
  }
}

Future<CelestialEvent> fetchCelestialEvent(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/celestialevents/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return CelestialEvent.fromJson(data);
  } else {
    throw Exception('Failed to load celestial event with id $id');
  }
}

Future<bool> createCelestialEvent(
  String name,
  String description,
  String uuid,
  int fkMoon,
  int fkSun,
  int fkMonth,
  int fkWeek,
  int fkDay,
  int eventYear,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/celestialevents'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_moon": fkMoon,
      "fk_sun": fkSun,
      "fk_month": fkMonth,
      "fk_week": fkWeek,
      "fk_day": fkDay,
      "event_year": eventYear,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editCelestialEvent(
  int id,
  String name,
  String description,
  String uuid,
  int fkMoon,
  int fkSun,
  int fkMonth,
  int fkWeek,
  int fkDay,
  int eventYear,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/celestialevents/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_moon": fkMoon,
      "fk_sun": fkSun,
      "fk_month": fkMonth,
      "fk_week": fkWeek,
      "fk_day": fkDay,
      "event_year": eventYear,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteCelestialEvent(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/celestialEvents/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("celestialEvent")
        : throw Exception('Failed to delete celestial event');
  }
}
