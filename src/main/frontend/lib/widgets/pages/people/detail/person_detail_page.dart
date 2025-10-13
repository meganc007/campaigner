import 'package:flutter/material.dart';
import 'package:frontend/models/people/person.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/person_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/people/add/add_person_page.dart';
import 'package:frontend/widgets/pages/people/edit/person_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class PersonDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> raceMap;
  final Map<int, String> wealthMap;
  final Map<int, String> abilityScoreMap;
  const PersonDetailPage({
    super.key,
    required this.uuid,
    required this.raceMap,
    required this.wealthMap,
    required this.abilityScoreMap,
  });

  @override
  State<PersonDetailPage> createState() => _PersonDetailPageState();
}

class _PersonDetailPageState extends State<PersonDetailPage> {
  late Future<List<Person>> _futurePeople;

  @override
  void initState() {
    super.initState();
    _futurePeople = fetchPeople(widget.uuid).then((list) {
      list.sort((a, b) => a.firstName.compareTo(b.firstName));
      return list;
    });
  }

  Future<void> _refreshData() async {
    setState(() {
      _futurePeople = fetchPeople(widget.uuid).then((list) {
        list.sort((a, b) => a.firstName.compareTo(b.firstName));
        return list;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("People".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futurePeople,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final people = snapshot.data!;

            final peopleWidgets = people
                .map(
                  (person) => DetailSection(
                    title: person.name,
                    fields: {
                      "Age": person.age.toString(),
                      "Title": person.title ?? 'Unknown',
                      "Race": widget.raceMap[person.fkRace] ?? 'Unknown',
                      "Wealth": widget.wealthMap[person.fkWealth] ?? 'Unknown',
                      "Ability Score":
                          widget.abilityScoreMap[person.fkAbilityScore] ??
                          'Unknown',
                      "NPC?": person.isNpc == true ? 'Yes' : 'No',
                      "Enemy?": person.isEnemy == true ? 'Yes' : 'No',
                      "Personality": person.personality ?? 'Unknown',
                      "Description": person.description ?? 'Unknown',
                      "Notes": person.notes ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              PersonEditPage(uuid: widget.uuid, person: person),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: person.name,
                        type: "person",
                        onDelete: () => deletePerson(person.id),
                        onSuccess: _refreshData,
                      );
                    },
                  ),
                )
                .toList();

            return SafeArea(
              child: Center(
                child: ListView(
                  padding: const EdgeInsets.all(12),
                  children: peopleWidgets
                      .map(
                        (widget) => Padding(
                          padding: const EdgeInsets.only(bottom: 12),
                          child: widget,
                        ),
                      )
                      .toList(),
                ),
              ),
            );
          },
        ),
      ),
      bottomNavigationBar: CreateNewButton(
        label: "Create Person",
        destinationBuilder: (context) => AddPersonPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
