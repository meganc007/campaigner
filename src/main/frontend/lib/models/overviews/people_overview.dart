import 'package:frontend/models/people/ability_score.dart';
import 'package:frontend/models/people/event_place_person.dart';
import 'package:frontend/models/people/generic_monster.dart';
import 'package:frontend/models/people/job.dart';
import 'package:frontend/models/people/job_assignment.dart';
import 'package:frontend/models/people/named_monster.dart';
import 'package:frontend/models/people/person.dart';
import 'package:frontend/models/people/race.dart';

class PeopleOverview {
  final List<AbilityScore> abilityScores;
  final List<EventPlacePerson> eventPlacePersons;
  final List<GenericMonster> genericMonsters;
  final List<JobAssignment> jobAssignments;
  final List<Job> jobs;
  final List<NamedMonster> namedMonsters;
  final List<Person> persons;
  final List<Race> races;

  const PeopleOverview({
    required this.abilityScores,
    required this.eventPlacePersons,
    required this.genericMonsters,
    required this.jobAssignments,
    required this.jobs,
    required this.namedMonsters,
    required this.persons,
    required this.races,
  });

  factory PeopleOverview.fromJson(Map<String, dynamic> json) {
    try {
      return PeopleOverview(
        abilityScores: (json['abilityScores'] as List<dynamic>)
            .map((e) => AbilityScore.fromJson(e as Map<String, dynamic>))
            .toList(),
        eventPlacePersons: (json['eventPlacePersons'] as List<dynamic>)
            .map((e) => EventPlacePerson.fromJson(e as Map<String, dynamic>))
            .toList(),
        genericMonsters: (json['genericMonsters'] as List<dynamic>)
            .map((e) => GenericMonster.fromJson(e as Map<String, dynamic>))
            .toList(),
        jobAssignments: (json['jobAssignments'] as List<dynamic>)
            .map((e) => JobAssignment.fromJson(e as Map<String, dynamic>))
            .toList(),
        jobs: (json['jobs'] as List<dynamic>)
            .map((e) => Job.fromJson(e as Map<String, dynamic>))
            .toList(),
        namedMonsters: (json['namedMonsters'] as List<dynamic>)
            .map((e) => NamedMonster.fromJson(e as Map<String, dynamic>))
            .toList(),
        persons: (json['persons'] as List<dynamic>)
            .map((e) => Person.fromJson(e as Map<String, dynamic>))
            .toList(),
        races: (json['races'] as List<dynamic>)
            .map((e) => Race.fromJson(e as Map<String, dynamic>))
            .toList(),
      );
    } catch (e) {
      throw FormatException("Failed to load PeopleOverview: $e");
    }
  }
}
