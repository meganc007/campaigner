import 'package:flutter/material.dart';
import 'package:frontend/models/people/ability_score.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/ability_score_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/people/add/add_ability_score_page.dart';
import 'package:frontend/widgets/pages/people/edit/ability_score_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class AbilityScoreDetailPage extends StatefulWidget {
  final String uuid;
  const AbilityScoreDetailPage({super.key, required this.uuid});

  @override
  State<AbilityScoreDetailPage> createState() => _AbilityScoreDetailPageState();
}

class _AbilityScoreDetailPageState extends State<AbilityScoreDetailPage> {
  late Future<List<AbilityScore>> _futureAbilityScores;

  @override
  void initState() {
    super.initState();
    setState(() {
      _futureAbilityScores = fetchAbilityScores(widget.uuid);
    });
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureAbilityScores = fetchAbilityScores(widget.uuid);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Ability Scores".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureAbilityScores,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final abilityScores = snapshot.data!;

            final abilityScoreWidgets = abilityScores
                .map(
                  (abilityScore) => DetailSection(
                    title: abilityScore.id.toString(),
                    fields: {
                      "Strength": abilityScore.strength.toString(),
                      "Dexterity": abilityScore.dexterity.toString(),
                      "Constitution": abilityScore.constitution.toString(),
                      "Intelligence": abilityScore.intelligence.toString(),
                      "Wisdom": abilityScore.wisdom.toString(),
                      "Charisma": abilityScore.charisma.toString(),
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => AbilityScoreEditPage(
                            uuid: widget.uuid,
                            abilityScore: abilityScore,
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
                        name: abilityScore.id.toString(),
                        type: "abilityScore",
                        onDelete: () => deleteAbilityScore(abilityScore.id),
                        onSuccess: _refreshData,
                      );
                    },
                  ),
                )
                .toList();

            return SafeArea(
              child: Center(
                child: ListView(
                  padding: EdgeInsets.all(12),
                  children: abilityScoreWidgets
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
        label: "Create Ability Score",
        destinationBuilder: (context) => AddAbilityScorePage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
