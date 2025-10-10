import 'dart:convert';

import 'package:frontend/models/people/generic_monster.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<GenericMonster>> fetchGenericMonsters(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/genericMonsters/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => GenericMonster.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load generic monsters');
  }
}

Future<GenericMonster> fetchGenericMonster(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/genericMonsters/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return GenericMonster.fromJson(data);
  } else {
    throw Exception('Failed to load generic monster with id $id');
  }
}

Future<bool> createGenericMonster(
  String name,
  String? description,
  int? fkAbilityScore,
  String? traits,
  String? notes,
  String uuid,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/genericMonsters'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      if (description != null) "description": description,
      if (fkAbilityScore != null) "fk_ability_score": fkAbilityScore,
      if (traits != null) "traits": traits,
      if (notes != null) "notes": notes,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editGenericMonster(
  int id,
  String name,
  String? description,
  int? fkAbilityScore,
  String? traits,
  String? notes,
  String uuid,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/genericMonsters/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      if (description != null) "description": description,
      if (fkAbilityScore != null) "fk_ability_score": fkAbilityScore,
      if (traits != null) "traits": traits,
      if (notes != null) "notes": notes,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteGenericMonster(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/genericMonsters/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("generic monster")
        : throw Exception('Failed to delete generic monster');
  }
}
