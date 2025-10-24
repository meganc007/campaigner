import 'dart:convert';

import 'package:frontend/models/people/named_monster.dart';
import 'package:frontend/services/api.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<NamedMonster>> fetchNamedMonsters(String uuid) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/namedMonsters/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => NamedMonster.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load named monsters');
  }
}

Future<NamedMonster> fetchNamedMonster(int id) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/namedMonsters/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return NamedMonster.fromJson(data);
  } else {
    throw Exception('Failed to load named monster with id $id');
  }
}

Future<bool> createNamedMonster(
  String firstName,
  String? lastName,
  String? title,
  int? fkWealth,
  int? fkAbilityScore,
  int? fkGenericMonster,
  bool? isEnemy,
  String personality,
  String description,
  String? notes,
  String uuid,
) async {
  final response = await http.post(
    Uri.parse('${Api.baseUrl}/namedMonsters'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "firstName": firstName,
      "lastName": lastName,
      "title": title,
      if (fkWealth != null) "fk_wealth": fkWealth,
      if (fkAbilityScore != null) "fk_ability_score": fkAbilityScore,
      if (fkGenericMonster != null) "fk_generic_monster": fkGenericMonster,
      if (isEnemy != null) "isEnemy": isEnemy,
      "personality": personality,
      "description": description,
      "notes": notes,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editNamedMonster(
  int id,
  String firstName,
  String? lastName,
  String? title,
  int? fkWealth,
  int? fkAbilityScore,
  int? fkGenericMonster,
  bool? isEnemy,
  String personality,
  String description,
  String? notes,
  String uuid,
) async {
  final response = await http.put(
    Uri.parse('${Api.baseUrl}/namedMonsters/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "firstName": firstName,
      "lastName": lastName,
      "title": title,
      if (fkWealth != null) "fk_wealth": fkWealth,
      if (fkAbilityScore != null) "fk_ability_score": fkAbilityScore,
      if (fkGenericMonster != null) "fk_generic_monster": fkGenericMonster,
      if (isEnemy != null) "isEnemy": isEnemy,
      "personality": personality,
      "description": description,
      "notes": notes,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteNamedMonster(int id) async {
  final response = await http.delete(
    Uri.parse('${Api.baseUrl}/namedMonsters/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("named monster")
        : throw Exception('Failed to delete named monster');
  }
}
