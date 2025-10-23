import 'dart:convert';

import 'package:frontend/models/calendar/month.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Month>> fetchMonths(String uuid) async {
  final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/months'));

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Month.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load months');
  }
}

Future<Month> fetchMonth(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/months/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Month.fromJson(data);
  } else {
    throw Exception('Failed to load month with id $id');
  }
}

Future<bool> createMonth(
  String name,
  String description,
  String uuid,
  String season,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/months'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "season": season,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editMonth(
  int id,
  String name,
  String description,
  String uuid,
  String season,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/months/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "season": season,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteMonth(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/months/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("month")
        : throw Exception('Failed to delete month');
  }
}
