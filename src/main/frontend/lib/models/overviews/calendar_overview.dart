import 'package:frontend/models/calendar/celestial_event.dart';
import 'package:frontend/models/calendar/day.dart';
import 'package:frontend/models/calendar/event.dart';
import 'package:frontend/models/calendar/month.dart';
import 'package:frontend/models/calendar/moon.dart';
import 'package:frontend/models/calendar/sun.dart';
import 'package:frontend/models/calendar/week.dart';

class CalendarOverview {
  final List<Sun> suns;
  final List<Moon> moons;
  final List<Month> months;
  final List<Week> weeks;
  final List<Day> days;
  final List<CelestialEvent> celestialEvents;
  final List<Event> events;

  const CalendarOverview({
    required this.suns,
    required this.moons,
    required this.months,
    required this.weeks,
    required this.days,
    required this.celestialEvents,
    required this.events,
  });

  factory CalendarOverview.fromJson(Map<String, dynamic> json) {
    try {
      return CalendarOverview(
        suns: (json['suns'] as List<dynamic>)
            .map((s) => Sun.fromJson(s as Map<String, dynamic>))
            .toList(),
        moons: (json['moons'] as List<dynamic>)
            .map((moon) => Moon.fromJson(moon as Map<String, dynamic>))
            .toList(),
        months: (json['months'] as List<dynamic>)
            .map((month) => Month.fromJson(month as Map<String, dynamic>))
            .toList(),
        weeks: (json['weeks'] as List<dynamic>)
            .map((w) => Week.fromJson(w as Map<String, dynamic>))
            .toList(),
        days: (json['days'] as List<dynamic>)
            .map((d) => Day.fromJson(d as Map<String, dynamic>))
            .toList(),
        celestialEvents: (json['celestialEvents'] as List<dynamic>)
            .map((ce) => CelestialEvent.fromJson(ce as Map<String, dynamic>))
            .toList(),
        events: (json['events'] as List<dynamic>)
            .map((ce) => Event.fromJson(ce as Map<String, dynamic>))
            .toList(),
      );
    } catch (e) {
      throw FormatException("Failed to load CalendarOverview: $e");
    }
  }
}
