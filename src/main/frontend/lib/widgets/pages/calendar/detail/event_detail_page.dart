import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/event.dart';
import 'package:frontend/services/calendar/event_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/calendar/add/add_event_page.dart';
import 'package:frontend/widgets/pages/calendar/edit/event_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class EventDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> monthMap;
  final Map<int, int> weekMap;
  final Map<int, String> dayMap;
  final Map<int, String> continentMap;
  final Map<int, String> countryMap;
  final Map<int, String> cityMap;
  const EventDetailPage({
    super.key,
    required this.uuid,
    required this.monthMap,
    required this.weekMap,
    required this.dayMap,
    required this.continentMap,
    required this.countryMap,
    required this.cityMap,
  });

  @override
  State<EventDetailPage> createState() => _EventDetailPageState();
}

class _EventDetailPageState extends State<EventDetailPage> {
  late Future<List<Event>> _futureEvents;

  @override
  void initState() {
    super.initState();
    _futureEvents = fetchEvents(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  Future<void> _refreshData() async {
    _futureEvents = fetchEvents(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Events".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureEvents,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final events = snapshot.data!;

            final eventWidgets = events
                .map(
                  (event) => DetailSection(
                    title: event.name,
                    fields: {
                      "Description": event.description,
                      "Event Year": event.eventYear.toString(),
                      "Month": widget.monthMap[event.fkMonth] ?? 'Unknown',
                      "Week": widget.weekMap[event.fkWeek].toString(),
                      "Day": widget.dayMap[event.fkDay] ?? 'Unknown',
                      "Continent":
                          widget.continentMap[event.fkContinent] ?? 'Unknown',
                      "Country":
                          widget.countryMap[event.fkCountry] ?? 'Unknown',
                      "City": widget.cityMap[event.fkCity] ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              EventEditPage(uuid: widget.uuid, event: event),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: event.name,
                        type: "event",
                        onDelete: () => deleteEvent(event.id),
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
                  children: eventWidgets
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
        label: "Create Event",
        destinationBuilder: (context) => AddEventPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
