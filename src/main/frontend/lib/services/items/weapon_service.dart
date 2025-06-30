import 'dart:convert';

import 'package:frontend/models/items/weapon.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Weapon>> fetchWeapons(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/weapons/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Weapon.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load weapons');
  }
}

Future<Weapon> fetchWeapon(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/weapons/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Weapon.fromJson(data);
  } else {
    throw Exception('Failed to load weapon with id $id');
  }
}

Future<bool> createWeapon(
  String uuid,
  String name,
  String description,
  String? rarity,
  int? goldValue,
  int? silverValue,
  int? copperValue,
  double? weight,
  int? fkWeaponType,
  int? fkDamageType,
  int? fkDiceType,
  int? numberOfDice,
  int? damageModifier,
  bool? isMagical,
  bool? isCursed,
  String? notes,
) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/weapons'),
    headers: {'Content-': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "rarity": rarity,
      "gold_value": goldValue,
      "silver_value": silverValue,
      "copper_value": copperValue,
      "weight": weight,
      "fk_weapon_type": fkWeaponType,
      "fk_damage_type": fkDamageType,
      "fk_dice_type": fkDiceType,
      "number_of_dice": numberOfDice,
      "damage_modifier": damageModifier,
      "isMagical": isMagical,
      "isCursed": isCursed,
      "notes": notes,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editWeapon(
  String uuid,
  int id,
  String name,
  String description,
  String? rarity,
  int? goldValue,
  int? silverValue,
  int? copperValue,
  double? weight,
  int? fkWeaponType,
  int? fkDamageType,
  int? fkDiceType,
  int? numberOfDice,
  int? damageModifier,
  bool? isMagical,
  bool? isCursed,
  String? notes,
) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/weapons/$id'),
    headers: {'Content-': 'application/json'},
    body: jsonEncode({
      "id": id,
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "rarity": rarity,
      "gold_value": goldValue,
      "silver_value": silverValue,
      "copper_value": copperValue,
      "weight": weight,
      "fk_weapon_type": fkWeaponType,
      "fk_damage_type": fkDamageType,
      "fk_dice_type": fkDiceType,
      "number_of_dice": numberOfDice,
      "damage_modifier": damageModifier,
      "isMagical": isMagical,
      "isCursed": isCursed,
      "notes": notes,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteWeapon(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/weapons/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("weapon")
        : throw Exception('Failed to delete weapon');
  }
}
