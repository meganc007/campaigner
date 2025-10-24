import 'dart:convert';

import 'package:frontend/models/items/inventory.dart';
import 'package:frontend/services/api.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Inventory>> fetchInventories(String uuid) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/inventory/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Inventory.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load inventories');
  }
}

Future<Inventory> fetchInventory(int id) async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/inventory/$id'));

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Inventory.fromJson(data);
  } else {
    throw Exception('Failed to load inventory with id $id');
  }
}

Future<bool> createInventory(
  String uuid,
  int? fkPerson,
  int? fkItem,
  int? fkWeapon,
  int? fkPlace,
) async {
  final response = await http.post(
    Uri.parse('${Api.baseUrl}/inventory'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "fk_campaign_uuid": uuid,
      if (fkPerson != null) "fk_person": fkPerson,
      if (fkItem != null) "fk_item": fkItem,
      if (fkWeapon != null) "fk_weapon": fkWeapon,
      if (fkPlace != null) "fk_place": fkPlace,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editInventory(
  String uuid,
  int id,
  int? fkPerson,
  int? fkItem,
  int? fkWeapon,
  int? fkPlace,
) async {
  final response = await http.put(
    Uri.parse('${Api.baseUrl}/inventory/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "id": id,
      "fk_campaign_uuid": uuid,
      if (fkPerson != null) "fk_person": fkPerson,
      if (fkItem != null) "fk_item": fkItem,
      if (fkWeapon != null) "fk_weapon": fkWeapon,
      if (fkPlace != null) "fk_place": fkPlace,
    }),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteInventory(int id) async {
  final response = await http.delete(Uri.parse('${Api.baseUrl}/inventory/$id'));

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("inventory")
        : throw Exception('Failed to delete inventory');
  }
}
