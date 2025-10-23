import 'dart:convert';

import 'package:frontend/models/common/government.dart';
import 'package:http/http.dart' as http;

Future<List<Government>> fetchGovernments() async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/governments'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Government.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load governments');
  }
}

Future<Government> fetchGovernment(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/governments/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Government.fromJson(data);
  } else {
    throw Exception('Failed to load government with id $id');
  }
}
