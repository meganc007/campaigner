import 'dart:convert';

import 'package:frontend/models/items/inventory.dart';
import 'package:frontend/models/items_overview.dart';
import 'package:http/http.dart' as http;

Future<ItemsOverview> fetchItemsOverview(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/items-overview/$uuid'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return ItemsOverview.fromJson(data);
  } else {
    throw Exception('Failed to load items overview');
  }
}

Future<List<Inventory>> fetchInventoriesFromItemsOverview(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/items-overview/$uuid/inventories'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Inventory.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load inventories from items overview');
  }
}
