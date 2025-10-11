import 'package:flutter/material.dart';
import 'package:frontend/models/people/named_monster.dart';
import 'package:frontend/services/people/named_monster_service.dart';

class NamedMonsterDetailPage extends StatefulWidget {
  final String uuid;
  const NamedMonsterDetailPage({super.key, required this.uuid});

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
    return const Placeholder(child: Text("under construction"));
  }
}
