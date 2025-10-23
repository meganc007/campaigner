import 'dart:convert';

import 'package:frontend/models/calendar/event.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Event>> fetchEvents(String uuid) async {
  final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/events'));

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Event.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load Events');
  }
}

Future<Event> fetchEvent(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/events/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Event.fromJson(data);
  } else {
    throw Exception('Failed to load event with id $id');
  }
}

Future<bool> createEvent(
  String name,
  String description,
  int eventYear,
  int fkMonth,
  int fkWeek,
  int fkDay,
  int fkContinent,
  int fkCountry,
  int fkCity,
  String uuid,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/events'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "event_year": eventYear,
      "fk_month": fkMonth,
      "fk_week": fkWeek,
      "fk_day": fkDay,
      "fk_continent": fkContinent,
      "fk_country": fkCountry,
      "fk_city": fkCity,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editEvent(
  int id,
  String name,
  String description,
  int eventYear,
  int fkMonth,
  int fkWeek,
  int fkDay,
  int fkContinent,
  int fkCountry,
  int fkCity,
  String uuid,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/events'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "event_year": eventYear,
      "fk_month": fkMonth,
      "fk_week": fkWeek,
      "fk_day": fkDay,
      "fk_continent": fkContinent,
      "fk_country": fkCountry,
      "fk_city": fkCity,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteEvent(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/events/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("event")
        : throw Exception('Failed to delete event');
  }
}
