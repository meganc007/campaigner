import 'package:flutter/material.dart';
import 'package:frontend/models/people/ability_score.dart';
import 'package:frontend/models/people/event_place_person.dart';
import 'package:frontend/models/people/generic_monster.dart';
import 'package:frontend/models/people/job.dart';
import 'package:frontend/models/people/job_assignment.dart';
import 'package:frontend/models/people/named_monster.dart';
import 'package:frontend/models/people/person.dart';
import 'package:frontend/models/people/race.dart';
import 'package:frontend/models/wealth.dart';
import 'package:frontend/services/people/ability_score_service.dart';
import 'package:frontend/services/people/event_place_person_service.dart';
import 'package:frontend/services/people/generic_monster_service.dart';
import 'package:frontend/services/people/job_assignment_service.dart';
import 'package:frontend/services/people/job_service.dart';
import 'package:frontend/services/people/named_monster_service.dart';
import 'package:frontend/services/people/person_service.dart';
import 'package:frontend/services/people/race_service.dart';
import 'package:frontend/services/wealth_service.dart';

class PeopleDataProvider extends ChangeNotifier {
  final String uuid;

  PeopleDataProvider(this.uuid);

  bool _initialized = false;

  List<AbilityScore> _abilityScores = [];
  List<EventPlacePerson> _eventPlacePersons = [];
  List<JobAssignment> _jobAssignments = [];
  List<Person> _people = [];
  // List<Race> _races = [];

  List<Person> get abilityScores => List.unmodifiable(_abilityScores);
  List<EventPlacePerson> get eventPlacePersons =>
      List.unmodifiable(_eventPlacePersons);
  List<JobAssignment> get jobAssignments => List.unmodifiable(_jobAssignments);
  List<Person> get people => List.unmodifiable(_people);
  // List<Person> get races => List.unmodifiable(_races);

  Map<int, String> _abilityScoreMap = {};
  //Map<int, String> _eventPlacePersonMap = {};
  Map<int, String> _genericMonsterMap = {};
  //Map<int, String> _jobAssignmentMap = {};
  Map<int, String> _jobMap = {};
  Map<int, String> _namedMonsterMap = {};
  Map<int, String> _personMap = {};
  Map<int, String> _raceMap = {};
  Map<int, String> _wealthMap = {};

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  Map<int, String> get abilityScoreMap => Map.unmodifiable(_abilityScoreMap);
  // Map<int, String> get eventPlacePersonMap =>
  //     Map.unmodifiable(_eventPlacePersonMap);
  Map<int, String> get genericMonsterMap =>
      Map.unmodifiable(_genericMonsterMap);
  // Map<int, String> get jobAssignmentMap => Map.unmodifiable(_jobAssignmentMap);
  Map<int, String> get jobMap => Map.unmodifiable(_jobMap);
  Map<int, String> get namedMonsterMap => Map.unmodifiable(_namedMonsterMap);
  Map<int, String> get personMap => Map.unmodifiable(_personMap);
  Map<int, String> get raceMap => Map.unmodifiable(_raceMap);
  Map<int, String> get wealthMap => Map.unmodifiable(_wealthMap);

  Future<void> load() async {
    if (_initialized) return;
    _initialized = true;
    _isLoading = true;
    notifyListeners();

    try {
      final results = await Future.wait([
        fetchAbilityScores(),
        fetchEventPlacePersons(uuid),
        fetchGenericMonsters(uuid),
        fetchJobAssignments(uuid),
        fetchJobs(),
        fetchNamedMonsters(uuid),
        fetchPeople(uuid),
        fetchRaces(),
        fetchWealth(),
      ]);

      final abilityScores = results[0] as List<AbilityScore>;
      final eventPlacePersons = results[1] as List<EventPlacePerson>;
      final genericMonsters = results[2] as List<GenericMonster>;
      final jobAssignments = results[3] as List<JobAssignment>;
      final jobs = results[4] as List<Job>;
      final namedMonsters = results[5] as List<NamedMonster>;
      final people = results[6] as List<Person>;
      final races = results[7] as List<Race>;
      final wealth = results[8] as List<Wealth>;

      _abilityScores = abilityScores;
      _eventPlacePersons = eventPlacePersons;
      _jobAssignments = jobAssignments;
      _people = people;

      _abilityScoreMap = {for (var as in abilityScores) as.id: as.toString()};
      // _eventPlacePersonMap = {};
      _genericMonsterMap = {for (var gm in genericMonsters) gm.id: gm.name};
      // _jobAssignmentMap = {};
      _jobMap = {for (var j in jobs) j.id: j.name};
      _namedMonsterMap = {
        for (var nm in namedMonsters)
          nm.id: '${nm.firstName} ${nm.lastName ?? ''}'.trim(),
      };
      _personMap = {
        for (var p in people) p.id: '${p.firstName} ${p.lastName ?? ''}'.trim(),
      };
      _raceMap = {for (var r in races) r.id: r.name};
      _wealthMap = {for (var w in wealth) w.id: w.name};
    } catch (e) {
      _isLoading = false;
      notifyListeners();
      throw Exception('Failed to load people data: $e');
    }

    _isLoading = false;
    notifyListeners();
  }

  void updateGenericMonster(int id, String name) {
    if (_genericMonsterMap[id] != name) {
      _genericMonsterMap[id] = name;
      notifyListeners();
    }
  }

  void updateJob(int id, String name) {
    if (_jobMap[id] != name) {
      _jobMap[id] = name;
      notifyListeners();
    }
  }

  void updateNamedMonster(int id, String name) {
    if (_namedMonsterMap[id] != name) {
      _namedMonsterMap[id] = name;
      notifyListeners();
    }
  }

  void updateRace(int id, String name) {
    if (_raceMap[id] != name) {
      _raceMap[id] = name;
      notifyListeners();
    }
  }
}
