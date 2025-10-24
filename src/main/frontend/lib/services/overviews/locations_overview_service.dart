import 'dart:convert';

import 'package:frontend/models/overviews/locations_overview.dart';
import 'package:frontend/services/api.dart';
import 'package:http/http.dart' as http;

Future<LocationsOverview> fetchLocationsOverview(String uuid) async {
  final response = await http.get(Uri.parse('${Api.baseUrl}/locations/$uuid'));

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return LocationsOverview.fromJson(data);
  } else {
    throw Exception('Failed to load location overview');
  }
}
