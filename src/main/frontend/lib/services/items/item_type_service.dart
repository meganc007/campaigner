import 'dart:convert';

import 'package:frontend/models/items/item_type.dart';
import 'package:frontend/services/error_handling.dart';
import 'package:http/http.dart' as http;

Future<List<ItemType>> fetchItemTypes() async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/itemtypes'),
  );
  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => ItemType.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load item types');
  }
}

Future<ItemType> fetchItemType(int id) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/itemtypes/$id'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return ItemType.fromJson(data);
  } else {
    throw Exception('Failed to load item types with id $id');
  }
}

Future<bool> createItemType(String name, String description) async {
  final response = await http.post(
    Uri.parse('http://10.0.2.2:8080/api/itemtypes'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<bool> editItemType(int id, String name, String description) async {
  final response = await http.put(
    Uri.parse('http://10.0.2.2:8080/api/itemtypes/$id'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({"id": id, "name": name, "description": description}),
  );

  return response.statusCode >= 200 && response.statusCode < 300;
}

Future<void> deleteItemType(int id) async {
  final response = await http.delete(
    Uri.parse('http://10.0.2.2:8080/api/itemtypes/$id'),
  );

  if (response.statusCode != 204 && response.statusCode != 200) {
    response.body.contains('foreign key constraint')
        ? throw ForeignKeyConstraintException("itemtypes")
        : throw Exception('Failed to delete item types');
  }
}
