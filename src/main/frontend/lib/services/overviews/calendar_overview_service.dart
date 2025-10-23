import 'dart:convert';

import 'package:frontend/models/overviews/calendar_overview.dart';
import 'package:http/http.dart' as http;

Future<CalendarOverview> fetchCalendarOverview(String uuid) async {
  final response = await http.get(
    Uri.parse('http://10.0.2.2:8080/api/calendar-overview/$uuid'),
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> data = json.decode(response.body);
    return CalendarOverview.fromJson(data);
  } else {
    throw Exception('Failed to load calendar overview');
  }
}
