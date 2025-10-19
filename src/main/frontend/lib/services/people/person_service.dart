import 'dart:convert';

import 'package:frontend/models/people/person.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Person>> fetchPeople(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/people/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Person.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load people');
  }
}

Future<Person> fetchPerson(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/people/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Person.fromJson(data);
  } else {
    throw Exception('Failed to load person with id $id');
  }
}

Future<bool> createPerson(
  String firstName,
  String? lastName,
  int? age,
  String? title,
  int? fkRace,
  int? fkWealth,
  int? fkAbilityScore,
  bool isNpc,
  bool isEnemy,
  String? personality,
  String? description,
  String? notes,
  String uuid,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/people'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "firstName": firstName,
      if (lastName != null) "lastName": lastName,
      if (age != null) "age": age,
      if (title != null && title.isNotEmpty) "title": title,
      if (fkRace != null) "fk_race": fkRace,
      if (fkWealth != null) "fk_wealth": fkWealth,
      if (fkAbilityScore != null) "fk_ability_score": fkAbilityScore,
      "isNPC": isNpc,
      "isEnemy": isEnemy,
      if (personality != null) "personality": personality,
      if (description != null) "description": description,
      if (notes != null) "notes": notes,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editPerson(
  int id,
  String firstName,
  String? lastName,
  int? age,
  String? title,
  int? fkRace,
  int? fkWealth,
  int? fkAbilityScore,
  bool isNpc,
  bool isEnemy,
  String? personality,
  String? description,
  String? notes,
  String uuid,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/people/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "firstName": firstName,
      if (lastName != null) "lastName": lastName,
      if (age != null) "age": age,
      if (title != null) "title": title,
      if (fkRace != null) "fk_race": fkRace,
      if (fkWealth != null) "fk_wealth": fkWealth,
      if (fkAbilityScore != null) "fk_ability_score": fkAbilityScore,
      "isNPC": isNpc,
      "isEnemy": isEnemy,
      if (personality != null) "personality": personality,
      if (description != null) "description": description,
      if (notes != null) "notes": notes,
      "fk_campaign_uuid": uuid,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deletePerson(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/people/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("person")
        : throw Exception('Failed to delete person');
  }
}
