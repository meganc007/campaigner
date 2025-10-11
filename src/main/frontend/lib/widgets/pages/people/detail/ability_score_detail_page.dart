import 'package:flutter/material.dart';
import 'package:frontend/models/people/ability_score.dart';
import 'package:frontend/services/people/ability_score_service.dart';

class AbilityScoreDetailPage extends StatefulWidget {
  final String? uuid;
  const AbilityScoreDetailPage({super.key, this.uuid});

  @override
  State<AbilityScoreDetailPage> createState() => _AbilityScoreDetailPageState();
}

class _AbilityScoreDetailPageState extends State<AbilityScoreDetailPage> {
  late Future<List<AbilityScore>> _futureAbilityScores;

  @override
  void initState() {
    super.initState();
    setState(() {
      _futureAbilityScores = fetchAbilityScores();
    });
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureAbilityScores = fetchAbilityScores();
    });
  }

  @override
  Widget build(BuildContext context) {
    return const Placeholder(child: Text("under construction"));
  }
}
