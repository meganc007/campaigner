import 'package:flutter/material.dart';
import 'package:frontend/models/people/event_place_person.dart';
import 'package:frontend/services/people_overview_service.dart';

class EventPlacePersonDetailPage extends StatefulWidget {
  final String uuid;
  const EventPlacePersonDetailPage({super.key, required this.uuid});

  @override
  State<EventPlacePersonDetailPage> createState() =>
      _EventPlacePersonDetailPageState();
}

class _EventPlacePersonDetailPageState
    extends State<EventPlacePersonDetailPage> {
  late Future<List<EventPlacePerson>> _futureEventPlacePersons;

  @override
  void initState() {
    super.initState();
    setState(() {
      _futureEventPlacePersons = fetchEventPlacePersonsFromPeopleOverview(
        widget.uuid,
      );
    });
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureEventPlacePersons = fetchEventPlacePersonsFromPeopleOverview(
        widget.uuid,
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    return const Placeholder(child: Text("under construction"));
  }
}
