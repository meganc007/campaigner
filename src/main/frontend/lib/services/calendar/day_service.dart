import 'dart:convert';

import 'package:frontend/models/calendar/day.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Day>> fetchDays(String uuid) async {
  final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/days'));

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Day.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load days');
  }
}

Future<Day> fetchDay(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/days/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Day.fromJson(data);
  } else {
    throw Exception('Failed to load day with id $id');
  }
}

Future<bool> createDay(
  String name,
  String description,
  String uuid,
  int fkWeek,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/days'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_week": fkWeek,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editDay(
  int id,
  String name,
  String description,
  String uuid,
  int fkWeek,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/days/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "fk_week": fkWeek,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteDay(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/days/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("day")
        : throw Exception('Failed to delete day');
  }
}
