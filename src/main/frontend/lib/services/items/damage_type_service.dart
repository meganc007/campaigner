import 'dart:convert';

import 'package:frontend/models/items/damage_type.dart';
import 'package:http/http.dart' as http;

Future<List<DamageType>> fetchDamageTypes() async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/damagetypes'),
  );
  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => DamageType.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load damage types');
  }
}
