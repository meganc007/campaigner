import 'dart:convert';

import 'package:frontend/models/people/job_assignment.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<JobAssignment>> fetchJobAssignments(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/jobAssignments/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => JobAssignment.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load job assignments');
  }
}

Future<JobAssignment> fetchJobAssignment(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/jobAssignments/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return JobAssignment.fromJson(data);
  } else {
    throw Exception('Failed to load job assignment with id $id');
  }
}

Future<bool> createJobAssignment(int? fkPerson, int? fkJob, String uuid) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/jobAssignments'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      if (fkPerson != null) "fk_person": fkPerson,
      if (fkJob != null) "fk_job": fkJob,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editJobAssignment(
  int id,
  int? fkPerson,
  int? fkJob,
  String uuid,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/jobAssignments/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      if (fkPerson != null) "fk_person": fkPerson,
      if (fkJob != null) "fk_job": fkJob,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteJobAssignment(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/jobAssignments/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("job assignment")
        : throw Exception('Failed to delete job assignment');
  }
}
