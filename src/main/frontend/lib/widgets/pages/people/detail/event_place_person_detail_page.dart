import 'package:flutter/material.dart';
import 'package:frontend/models/people/event_place_person.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/overviews/people_overview_service.dart';
import 'package:frontend/services/people/event_place_person_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/people/add/add_event_place_person_page.dart';
import 'package:frontend/widgets/pages/people/edit/event_place_person_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

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

  String showEventPlacePersonName(EventPlacePerson epp) {
    if (epp.placeName != null) {
      return epp.placeName!;
    }

    if (epp.personName != null) {
      return epp.personName!;
    }

    return "Unknown";
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Events Places Persons".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureEventPlacePersons,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final eventsPlacesPeople = snapshot.data!;

            final eventsPlacesPeopleWidgets = eventsPlacesPeople
                .map(
                  (epp) => DetailSection(
                    title: "#${epp.id}",
                    fields: {
                      "Person": epp.personName ?? 'Unknown',
                      "Event": epp.eventName ?? 'Unknown',
                      "Place": epp.placeName ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => EventPlacePersonEditPage(
                            uuid: widget.uuid,
                            eventPlacePerson: epp,
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
                        name: showEventPlacePersonName(epp),
                        type: "eventPlacePerson",
                        onDelete: () => deleteEventPlacePerson(epp.id),
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
                  children: eventsPlacesPeopleWidgets
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
        label: "Create Event Place Person",
        destinationBuilder: (context) =>
            AddEventPlacePersonPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
