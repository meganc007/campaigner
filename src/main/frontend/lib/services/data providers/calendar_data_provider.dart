import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/celestial_event.dart';
import 'package:frontend/models/calendar/day.dart';
import 'package:frontend/models/calendar/month.dart';
import 'package:frontend/models/calendar/moon.dart';
import 'package:frontend/models/calendar/sun.dart';
import 'package:frontend/models/calendar/week.dart';
import 'package:frontend/services/calendar/celestial_event_service.dart';
import 'package:frontend/services/calendar/day_service.dart';
import 'package:frontend/services/calendar/month_service.dart';
import 'package:frontend/services/calendar/moon_service.dart';
import 'package:frontend/services/calendar/sun_service.dart';
import 'package:frontend/services/calendar/week_service.dart';

class CalendarDataProvider extends ChangeNotifier {
  final String uuid;

  CalendarDataProvider(this.uuid);

  bool _initialized = false;

  List<Sun> _suns = [];
  List<Moon> _moons = [];
  List<Month> _months = [];
  List<Week> _weeks = [];
  List<Day> _days = [];
  List<CelestialEvent> _celestialEvents = [];

  List<Sun> get suns => List.unmodifiable(_suns);
  List<Moon> get moons => List.unmodifiable(_moons);
  List<Month> get months => List.unmodifiable(_months);
  List<Week> get weeks => List.unmodifiable(_weeks);
  List<Day> get days => List.unmodifiable(_days);
  List<CelestialEvent> get celestialEvents =>
      List.unmodifiable(_celestialEvents);

  Map<int, String> _weekMap = {};
  Map<int, String> _dayMap = {};
  Map<int, String> _celestialEventMap = {};

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  Map<int, String> get weekMap => Map.unmodifiable(_weekMap);
  Map<int, String> get dayMap => Map.unmodifiable(_dayMap);
  Map<int, String> get celestialEventMap =>
      Map.unmodifiable(_celestialEventMap);

  Future<void> load() async {
    if (_initialized) return;
    _initialized = true;
    _isLoading = true;
    notifyListeners();

    try {
      final results = await Future.wait([
        fetchSuns(uuid),
        fetchMoons(uuid),
        fetchMonths(uuid),
        fetchWeeks(uuid),
        fetchDays(uuid),
        fetchCelestialEvents(uuid),
      ]);

      final suns = results[0] as List<Sun>;
      final moons = results[1] as List<Moon>;
      final months = results[2] as List<Month>;
      final weeks = results[3] as List<Week>;
      final days = results[4] as List<Day>;
      final celestialEvents = results[5] as List<CelestialEvent>;

      _suns = suns;
      _moons = moons;
      _months = months;
      _weeks = weeks;
      _days = days;
      _celestialEvents = celestialEvents;

      _weekMap = {for (var w in weeks) w.id: w.name};
      _dayMap = {for (var d in days) d.id: d.name};
      _celestialEventMap = {for (var ce in celestialEvents) ce.id: ce.name};
    } catch (e) {
      _isLoading = false;
      notifyListeners();
      throw Exception('Failed to load calendar data: $e');
    }

    _isLoading = false;
    notifyListeners();
  }

  void updateWeek(int id, String name) {
    if (_weekMap[id] != name) {
      _weekMap[id] = name;
      notifyListeners();
    }
  }

  void updateDay(int id, String name) {
    if (_dayMap[id] != name) {
      _dayMap[id] = name;
      notifyListeners();
    }
  }

  void updateCelestialEvent(int id, String name) {
    if (_celestialEventMap[id] != name) {
      _celestialEventMap[id] = name;
      notifyListeners();
    }
  }
}
