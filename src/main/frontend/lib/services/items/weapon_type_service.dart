import 'dart:convert';

import 'package:frontend/models/items/weapon_type.dart';
import 'package:http/http.dart' as http;

Future<List<WeaponType>> fetchWeaponTypes() async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/weapontypes'),
  );
  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => WeaponType.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load weapon types');
  }
}
