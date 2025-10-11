import 'package:flutter/material.dart';
import 'package:frontend/models/people/person.dart';
import 'package:frontend/services/people/person_service.dart';

class PeopleDetailPage extends StatefulWidget {
  final String uuid;
  const PeopleDetailPage({super.key, required this.uuid});

  @override
  State<PeopleDetailPage> createState() => _PeopleDetailPageState();
}

class _PeopleDetailPageState extends State<PeopleDetailPage> {
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
    return const Placeholder(child: Text("under construction"));
  }
}
