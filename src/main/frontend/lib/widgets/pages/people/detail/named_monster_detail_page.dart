import 'package:flutter/material.dart';
import 'package:frontend/models/people/named_monster.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/named_monster_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/people/add/add_named_monster_page.dart';
import 'package:frontend/widgets/pages/people/edit/named_monster_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class NamedMonsterDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> wealthMap;
  final Map<int, String> genericMonsterMap;
  final Map<int, String> abilityScoreMap;
  const NamedMonsterDetailPage({
    super.key,
    required this.uuid,
    required this.wealthMap,
    required this.abilityScoreMap,
    required this.genericMonsterMap,
  });

  @override
  State<NamedMonsterDetailPage> createState() => _NamedMonsterDetailPageState();
}

class _NamedMonsterDetailPageState extends State<NamedMonsterDetailPage> {
  late Future<List<NamedMonster>> _futureNamedMonsters;

  @override
  void initState() {
    super.initState();
    setState(() {
      _futureNamedMonsters = fetchNamedMonsters(widget.uuid).then((list) {
        list.sort((a, b) => a.name.compareTo(b.name));
        return list;
      });
    });
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureNamedMonsters = fetchNamedMonsters(widget.uuid).then((list) {
        list.sort((a, b) => a.name.compareTo(b.name));
        return list;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Named Monsters".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureNamedMonsters,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final namedMonsters = snapshot.data!;
            namedMonsters.sort((a, b) => a.name.compareTo(b.name));

            final namedMonsterWidgets = namedMonsters
                .map(
                  (nm) => DetailSection(
                    title: nm.name,
                    fields: {
                      "Title": nm.title ?? 'Unknown',
                      "Wealth": widget.wealthMap[nm.fkWealth] ?? 'Unknown',
                      "Ability Score":
                          widget.abilityScoreMap[nm.fkAbilityScore] ??
                          'Unknown',
                      "Generic Monster":
                          widget.genericMonsterMap[nm.fkGenericMonster] ??
                          'Unknown',
                      "Enemy?": nm.isEnemy == true ? 'Yes' : 'No',
                      "Personality": nm.personality,
                      "Description": nm.description,
                      "Notes": nm.notes ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => NamedMonsterEditPage(
                            uuid: widget.uuid,
                            namedMonster: nm,
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
                        name: nm.name,
                        type: "named monster",
                        onDelete: () => deleteNamedMonster(nm.id),
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
                  children: namedMonsterWidgets
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
        label: "Create Named Monster",
        destinationBuilder: (context) => AddNamedMonsterPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
