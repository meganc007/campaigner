import 'dart:convert';

import 'package:frontend/models/people/ability_score.dart';
import 'package:frontend/services/api.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<AbilityScore>> fetchAbilityScores(String uuid) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/abilityScores/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => AbilityScore.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load ability scores');
  }
}

Future<AbilityScore> fetchAbilityScore(int id) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/abilityScores/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return AbilityScore.fromJson(data);
  } else {
    throw Exception('Failed to load ability score with id $id');
  }
}

Future<bool> createAbilityScore(
  String uuid,
  int strength,
  int dexterity,
  int constitution,
  int intelligence,
  int wisdom,
  int charisma,
) async {
  final response = await http.post(
    Uri.parse('${Api.baseUrl}/abilityScores'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "fk_campaign_uuid": uuid,
      "strength": strength,
      "dexterity": dexterity,
      "constitution": constitution,
      "intelligence": intelligence,
      "wisdom": wisdom,
      "charisma": charisma,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editAbilityScore(
  String uuid,
  int id,
  int strength,
  int dexterity,
  int constitution,
  int intelligence,
  int wisdom,
  int charisma,
) async {
  final response = await http.put(
    Uri.parse('${Api.baseUrl}/abilityScores/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "fk_campaign_uuid": uuid,
      "strength": strength,
      "dexterity": dexterity,
      "constitution": constitution,
      "intelligence": intelligence,
      "wisdom": wisdom,
      "charisma": charisma,
    }),
  );
  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteAbilityScore(int id) async {
  final response = await http.delete(
    Uri.parse('${Api.baseUrl}/abilityScores/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("ability score")
        : throw Exception('Failed to delete ability score');
  }
}
