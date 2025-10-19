import 'package:flutter/material.dart';
import 'package:frontend/models/people/race.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/race_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/people/add/add_race_page.dart';
import 'package:frontend/widgets/pages/people/edit/race_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class RaceDetailPage extends StatefulWidget {
  const RaceDetailPage({super.key});

  @override
  State<RaceDetailPage> createState() => _RaceDetailPageState();
}

class _RaceDetailPageState extends State<RaceDetailPage> {
  late Future<List<Race>> _futureRaces;

  @override
  void initState() {
    super.initState();
    setState(() {
      _futureRaces = fetchRaces().then((list) {
        list.sort((a, b) => a.name.compareTo(b.name));
        return list;
      });
    });
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureRaces = fetchRaces().then((list) {
        list.sort((a, b) => a.name.compareTo(b.name));
        return list;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Races".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureRaces,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final races = snapshot.data!;

            final raceWidgets = races
                .map(
                  (race) => DetailSection(
                    title: race.name,
                    fields: {
                      "Description": race.description,
                      "Exotic?": race.isExotic == true ? 'Yes' : 'No',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => RaceEditPage(race: race),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: race.name,
                        type: "race",
                        onDelete: () => deleteRace(race.id),
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
                  children: raceWidgets
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
        label: "Create Race",
        destinationBuilder: (context) => AddRacePage(),
        onReturn: _refreshData,
      ),
    );
  }
}
