import 'package:flutter/material.dart';
import 'package:frontend/models/people/ability_score.dart';
import 'package:frontend/models/overviews/people_overview.dart';
import 'package:frontend/widgets/reusable/section.dart';
import 'package:frontend/services/data%20providers/people_data_provider.dart';
import 'package:frontend/services/overviews/people_overview_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/overviews/overview_section.dart';
import 'package:frontend/widgets/pages/people/detail/ability_score_detail_page.dart';
import 'package:frontend/widgets/pages/people/detail/event_place_person_detail_page.dart';
import 'package:frontend/widgets/pages/people/detail/generic_monster_detail_page.dart';
import 'package:frontend/widgets/pages/people/detail/job_assignment_detail_page.dart';
import 'package:frontend/widgets/pages/people/detail/job_detail_page.dart';
import 'package:frontend/widgets/pages/people/detail/named_monster_detail_page.dart';
import 'package:frontend/widgets/pages/people/detail/person_detail_page.dart';
import 'package:frontend/widgets/pages/people/detail/race_detail_page.dart';
import 'package:provider/provider.dart';

class PeopleOverviewPage extends StatefulWidget {
  final String uuid;
  final Future<PeopleOverview> futurePeople;

  const PeopleOverviewPage({
    super.key,
    required this.uuid,
    required this.futurePeople,
  });

  @override
  State<PeopleOverviewPage> createState() => _PeopleOverviewPageState();
}

class _PeopleOverviewPageState extends State<PeopleOverviewPage> {
  late Future<PeopleOverview> _future;

  @override
  void initState() {
    super.initState();
    _future = fetchPeopleOverview(widget.uuid);

    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<PeopleDataProvider>().load();
    });
  }

  Future<void> _refreshData() async {
    setState(() {
      _future = fetchPeopleOverview(widget.uuid);
    });

    final provider = context.read<PeopleDataProvider>();
    await provider.load();
  }

  List<String> mapNamesToList(List<dynamic> items) {
    return items.map((item) => (item as dynamic).name as String).toList();
  }

  List<String> mapIdsToList(List<dynamic> items) {
    return items.map((item) => (item as dynamic).id.toString()).toList();
  }

  List<String> abilityScoresToString(List<dynamic> items) {
    return items.map((item) => (item as AbilityScore).toShortString()).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<PeopleDataProvider>(
      builder: (context, peopleDataProvider, child) {
        if (peopleDataProvider.isLoading) {
          return const Center(child: CircularProgressIndicator());
        }

        return RefreshIndicator(
          onRefresh: _refreshData,
          child: FutureBuilder<PeopleOverview>(
            future: _future,
            builder: (context, snapshot) {
              final validationResult = futureBuilderValidation(snapshot);
              if (validationResult != null) return validationResult;

              final peopleOverview = snapshot.data!;

              final Set<String> jobAssignmentNames = peopleOverview
                  .jobAssignments
                  .map((ja) => ja.personName ?? ja.jobName ?? '')
                  .where((name) => name.isNotEmpty)
                  .toSet();

              final Set<String> eventPlacePersonNames = peopleOverview
                  .eventPlacePersons
                  .map(
                    (epp) =>
                        epp.personName ?? epp.eventName ?? epp.placeName ?? '',
                  )
                  .where((name) => name.isNotEmpty)
                  .toSet();

              final List<String> sortedJobAssignmentNames =
                  jobAssignmentNames.toList()..sort();

              final List<String> sortedEventPlacePersonNames =
                  eventPlacePersonNames.toList()..sort();

              final List<Widget> sectionWidgets = [
                OverviewSection(
                  section: Section(
                    "People".toUpperCase(),
                    mapNamesToList(peopleOverview.persons),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => PersonDetailPage(
                          uuid: widget.uuid,
                          raceMap: peopleDataProvider.raceMap,
                          wealthMap: peopleDataProvider.wealthMap,
                          abilityScoreMap: peopleDataProvider.abilityScoreMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Generic Monsters".toUpperCase(),
                    mapNamesToList(peopleOverview.genericMonsters),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => GenericMonsterDetailPage(
                          uuid: widget.uuid,
                          abilityScoreMap: peopleDataProvider.abilityScoreMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Named Monsters".toUpperCase(),
                    mapNamesToList(peopleOverview.namedMonsters),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => NamedMonsterDetailPage(
                          uuid: widget.uuid,
                          wealthMap: peopleDataProvider.wealthMap,
                          abilityScoreMap: peopleDataProvider.abilityScoreMap,
                          genericMonsterMap:
                              peopleDataProvider.genericMonsterMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Races".toUpperCase(),
                    mapNamesToList(peopleOverview.races),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => RaceDetailPage()),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Jobs".toUpperCase(),
                    mapNamesToList(peopleOverview.jobs),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => JobDetailPage()),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Job Assignments".toUpperCase(),
                    sortedJobAssignmentNames,
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) =>
                            JobAssignmentDetailPage(uuid: widget.uuid),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Event Place Person".toUpperCase(),
                    sortedEventPlacePersonNames,
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) =>
                            EventPlacePersonDetailPage(uuid: widget.uuid),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Ability Scores".toUpperCase(),
                    mapIdsToList(peopleOverview.abilityScores),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) =>
                            AbilityScoreDetailPage(uuid: widget.uuid),
                      ),
                    );
                    await _refreshData();
                  },
                ),
              ];

              return SafeArea(
                child: SingleChildScrollView(
                  padding: const EdgeInsets.all(12),
                  child: Center(
                    child: Wrap(
                      spacing: 12,
                      runSpacing: 12,
                      children: sectionWidgets,
                    ),
                  ),
                ),
              );
            },
          ),
        );
      },
    );
  }
}
