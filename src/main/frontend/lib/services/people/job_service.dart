import 'dart:convert';

import 'package:frontend/models/people/job.dart';
import 'package:frontend/services/api.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Job>> fetchJobs() async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/jobs'));

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Job.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load jobs');
  }
}

Future<Job> fetchJob(int id) async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/jobs/$id'));

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Job.fromJson(data);
  } else {
    throw Exception('Failed to load job with id $id');
  }
}

Future<bool> createJob(String name, String description) async {
  final response = await http.post(
    Uri.parse('${Api.baseUrl}/jobs'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editJob(int id, String name, String description) async {
  final response = await http.put(
    Uri.parse('${Api.baseUrl}/jobs/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"id": id, "name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteJob(int id) async {
  final response = await http.delete(Uri.parse('${Api.baseUrl}/jobs/$id'));

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("job")
        : throw Exception('Failed to delete job');
  }
}
