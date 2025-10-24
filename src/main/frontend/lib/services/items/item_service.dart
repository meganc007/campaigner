import 'dart:convert';

import 'package:frontend/models/items/item.dart';
import 'package:frontend/services/api.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<Item>> fetchItems(String uuid) async {
  final response = await http.get(
    Uri.parse('${Api.baseUrl}/items/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Item.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load items');
  }
}

Future<Item> fetchItem(int id) async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/items/$id'));

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return Item.fromJson(data);
  } else {
    throw Exception('Failed to load item with id $id');
  }
}

Future<bool> createItem(
  String uuid,
  String name,
  String? description,

  String? rarity,
  int? goldValue,
  int? silverValue,
  int? copperValue,
  double? weight,
  int? fkItemType,
  bool? isMagical,
  bool? isCursed,
  String? notes,
) async {
  final response = await http.post(
    Uri.parse('${Api.baseUrl}/items'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({
      "name": name,
      "description": description,
      "fk_campaign_uuid": uuid,
      "rarity": rarity,
      "gold_value": goldValue,
      "silver_value": silverValue,
      "copper_value": copperValue,
      "weight": weight,
      "fk_item_type": fkItemType,
      "isMagical": isMagical,
      "isCursed": isCursed,
      "notes": notes,
    }),
  );
  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editItem(
  String uuid,
  int id,
  String name,
  String? description,
  String? rarity,
  int? goldValue,
  int? silverValue,
  int? copperValue,
  double? weight,
  int? fkItemType,
  bool? isMagical,
  bool? isCursed,
  String? notes,
) async {
  final response = await http.put(
    Uri.parse('${Api.baseUrl}/items/$id'),
    headers: {'Content-Type': 'application/json'},
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
      "fk_item_type": fkItemType,
      "isMagical": isMagical,
      "isCursed": isCursed,
      "notes": notes,
    }),
  );
  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteItem(int id) async {
  final response = await http.delete(Uri.parse('${Api.baseUrl}/items/$id'));

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("item")
        : throw Exception('Failed to delete item');
  }
}
