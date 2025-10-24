import 'dart:convert';

import 'package:frontend/models/people/event_place_person.dart';
import 'package:frontend/models/people/job_assignment.dart';
import 'package:frontend/models/overviews/people_overview.dart';
import 'package:frontend/services/api.dart';
import 'package:http/http.dart' as http;

Future<PeopleOverview> fetchPeopleOverview(String uuid) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/people-overview/$uuid'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return PeopleOverview.fromJson(data);
  } else {
    throw Exception('Failed to load people overview');
  }
}

Future<List<JobAssignment>> fetchJobAssignmentsFromPeopleOverview(
  String uuid,
) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/people-overview/$uuid/jobAssignments'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => JobAssignment.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load job assignments from people overview');
  }
}

Future<List<EventPlacePerson>> fetchEventPlacePersonsFromPeopleOverview(
  String uuid,
) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/people-overview/$uuid/eventPlacePersons'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => EventPlacePerson.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load eventPlacePersons from people overview');
  }
}
