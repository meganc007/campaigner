import 'package:flutter/material.dart';
import 'package:frontend/models/people/generic_monster.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/generic_monster_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/people/add/add_generic_monster_page.dart';
import 'package:frontend/widgets/pages/people/edit/generic_monster_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class GenericMonsterDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> abilityScoreMap;
  const GenericMonsterDetailPage({
    super.key,
    required this.uuid,
    required this.abilityScoreMap,
  });

  @override
  State<GenericMonsterDetailPage> createState() =>
      _GenericMonsterDetailPageState();
}

class _GenericMonsterDetailPageState extends State<GenericMonsterDetailPage> {
  late Future<List<GenericMonster>> _futureGenericMonsters;

  @override
  void initState() {
    super.initState();
    _futureGenericMonsters = fetchGenericMonsters(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureGenericMonsters = fetchGenericMonsters(widget.uuid).then((list) {
        list.sort((a, b) => a.name.compareTo(b.name));
        return list;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Generic Monsters".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureGenericMonsters,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final genericMonsters = snapshot.data!;
            genericMonsters.sort((a, b) => a.name.compareTo(b.name));

            final genericMonsterWidgets = genericMonsters
                .map(
                  (gm) => DetailSection(
                    title: gm.name,
                    fields: {
                      "Ability Score":
                          widget.abilityScoreMap[gm.fkAbilityScore] ??
                          'Unknown',
                      "Traits": gm.traits ?? 'Unknown',
                      "Description": gm.description ?? 'Unknown',
                      "Notes": gm.notes ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => GenericMonsterEditPage(
                            uuid: widget.uuid,
                            genericMonster: gm,
                          ),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: gm.name,
                        type: "generic monster",
                        onDelete: () => deleteGenericMonster(gm.id),
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
                  children: genericMonsterWidgets
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
        label: "Create Generic Monster",
        destinationBuilder: (context) =>
            AddGenericMonsterPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
