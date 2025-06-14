import 'dart:convert';

import 'package:frontend/models/items/item_type.dart';
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
