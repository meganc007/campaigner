import 'package:flutter/material.dart';
import 'package:frontend/models/people/race.dart';
import 'package:frontend/services/people/race_service.dart';

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
    return const Placeholder(child: Text("under construction"));
  }
}
