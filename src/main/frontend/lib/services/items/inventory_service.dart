import 'dart:convert';

import 'package:frontend/models/items/inventory.dart';
import 'package:http/http.dart' as http;

Future<List<Inventory>> fetchInventories(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/inventory/campaign/$uuid'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Inventory.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load inventories');
  }
}
