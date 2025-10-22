import 'dart:convert';

import 'package:frontend/models/calendar/week.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Week>> fetchWeeks(String uuid) async {
  final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/weeks'));

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Week.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load weeks');
  }
}

Future<Week> fetchWeek(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/weeks/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Week.fromJson(data);
  } else {
    throw Exception('Failed to load week with id $id');
  }
}

Future<bool> createWeek(
  String name,
  String description,
  String uuid,
  int weekNumber,
  int fkMonth,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/weeks'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "week_number": weekNumber,
      "fk_month": fkMonth,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editWeek(
  int id,
  String name,
  String description,
  String uuid,
  int weekNumber,
  int fkMonth,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/weeks/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "week_number": weekNumber,
      "fk_month": fkMonth,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteWeek(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/weeks/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("week")
        : throw Exception('Failed to delete week');
  }
}
