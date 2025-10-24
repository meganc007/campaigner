import 'dart:convert';

import 'package:frontend/models/people/event_place_person.dart';
import 'package:frontend/services/api.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<EventPlacePerson>> fetchEventPlacePersons(String uuid) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/eventsPlacesPeople/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => EventPlacePerson.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load eventPlacePersons');
  }
}

Future<EventPlacePerson> fetchEventPlacePerson(int id) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/eventsPlacesPeople/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return EventPlacePerson.fromJson(data);
  } else {
    throw Exception('Failed to load eventPlacePerson with id $id');
  }
}

Future<bool> createEventPlacePerson(
  int? fkEvent,
  int? fkPlace,
  int? fkPerson,
  String uuid,
) async {
  final response = await http.post(
    Uri.parse('${Api.baseUrl}/eventsPlacesPeople'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      if (fkEvent != null) "fk_event": fkEvent,
      if (fkPlace != null) "fk_place": fkPlace,
      if (fkPerson != null) "fk_person": fkPerson,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editEventPlacePerson(
  int id,
  int? fkEvent,
  int? fkPlace,
  int? fkPerson,
  String uuid,
) async {
  final response = await http.put(
    Uri.parse('${Api.baseUrl}/eventsPlacesPeople/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      if (fkEvent != null) "fk_event": fkEvent,
      if (fkPlace != null) "fk_place": fkPlace,
      if (fkPerson != null) "fk_person": fkPerson,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteEventPlacePerson(int id) async {
  final response = await http.delete(
    Uri.parse('${Api.baseUrl}/eventsPlacesPeople/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("eventPlacePerson")
        : throw Exception('Failed to delete eventPlacePerson');
  }
}
