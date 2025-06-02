import 'dart:convert';

import 'package:frontend/models/campaign.dart';
import 'package:http/http.dart' as http;

Future<List<Campaign>> fetchCampaigns() async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/campaigns'),
  );

  if (response.statusCode == 200) {
    final List<dynamic> data = json.decode(response.body);
    return data.map((json) => Campaign.fromJson(json)).toList();
  } else {
    throw Exception('Failed to load campaigns');
  }
}
