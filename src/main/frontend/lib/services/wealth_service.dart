import 'dart:convert';

import 'package:frontend/models/common/wealth.dart';
import 'package:http/http.dart' as http;

Future<List<Wealth>> fetchWealth() async {
  final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/wealth'));

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Wealth.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load wealth');
  }
}

Future<Wealth> fetchWealthById(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/wealth/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Wealth.fromJson(data);
  } else {
    throw Exception('Failed to load wealth with id $id');
  }
}

Future<bool> createWealth(String name, String description) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/wealth'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editWealth(int id, String name, String description) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/wealth/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"id": id, "name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteWealth(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/wealth/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    throw Exception('Failed to delete wealth');
  }
}
