import 'dart:convert';

import 'package:frontend/models/items/dice_type.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<DiceType>> fetchDiceTypes() async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/dicetypes'),
  );
  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => DiceType.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load dice types');
  }
}

Future<DiceType> fetchDiceType(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/dicetypes/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return DiceType.fromJson(data);
  } else {
    throw Exception('Failed to load dice types with id $id');
  }
}

Future<bool> createDiceType(
  String name,
  String description,
  int maxRoll,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/dicetypes'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "max_roll": maxRoll,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editDiceType(
  int id,
  String name,
  String description,
  int maxRoll,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/dicetypes'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "max_roll": maxRoll,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteDiceType(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/dicetypes/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("dicetypes")
        : throw Exception('Failed to delete dice types');
  }
}
