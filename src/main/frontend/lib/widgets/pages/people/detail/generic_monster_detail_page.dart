import 'package:flutter/material.dart';
import 'package:frontend/models/people/generic_monster.dart';
import 'package:frontend/services/people/generic_monster_service.dart';

class GenericMonsterDetailPage extends StatefulWidget {
  final String uuid;
  const GenericMonsterDetailPage({super.key, required this.uuid});

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
    return const Placeholder(child: Text("under construction"));
  }
}
