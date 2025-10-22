import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/celestial_event.dart';
import 'package:frontend/services/calendar/celestial_event_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/calendar/add/add_celestial_event_page.dart';
import 'package:frontend/widgets/pages/calendar/edit/celestial_event_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class CelestialEventDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> moonMap;
  final Map<int, String> sunMap;
  final Map<int, String> monthMap;
  final Map<int, String> weekMap;
  final Map<int, String> dayMap;
  const CelestialEventDetailPage({
    super.key,
    required this.uuid,
    required this.moonMap,
    required this.sunMap,
    required this.monthMap,
    required this.weekMap,
    required this.dayMap,
  });

  @override
  State<CelestialEventDetailPage> createState() =>
      _CelestialEventDetailPageState();
}

class _CelestialEventDetailPageState extends State<CelestialEventDetailPage> {
  late Future<List<CelestialEvent>> _futureCelestialEvents;

  @override
  void initState() {
    super.initState();
    _futureCelestialEvents = fetchCelestialEvents(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  Future<void> _refreshData() async {
    _futureCelestialEvents = fetchCelestialEvents(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Celestial Events".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureCelestialEvents,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final celestialEvents = snapshot.data!;

            final celestialEventWidgets = celestialEvents
                .map(
                  (celestialEvent) => DetailSection(
                    title: celestialEvent.name,
                    fields: {
                      "Description": celestialEvent.description,
                      "Moon":
                          widget.moonMap[celestialEvent.fkMoon] ?? 'Unknown',
                      "Sun": widget.sunMap[celestialEvent.fkSun] ?? 'Unknown',
                      "Month":
                          widget.monthMap[celestialEvent.fkMonth] ?? 'Unknown',
                      "Week":
                          widget.weekMap[celestialEvent.fkWeek] ?? 'Unknown',
                      "Day": widget.dayMap[celestialEvent.fkDay] ?? 'Unknown',
                      "Event Year": celestialEvent.eventYear.toString(),
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => CelestialEventEditPage(
                            uuid: widget.uuid,
                            celestialEvent: celestialEvent,
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
                        name: celestialEvent.name,
                        type: "celestialEvent",
                        onDelete: () => deleteCelestialEvent(celestialEvent.id),
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
                  children: celestialEventWidgets
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
        label: "Create Celestial Event",
        destinationBuilder: (context) =>
            AddCelestialEventPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
