import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/event.dart';
import 'package:frontend/models/location/place.dart';
import 'package:frontend/models/people/event_place_person.dart';
import 'package:frontend/models/people/person.dart';
import 'package:frontend/services/calendar/event_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/locations/place_service.dart';
import 'package:frontend/services/people/event_place_person_service.dart';
import 'package:frontend/services/people/person_service.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class EventPlacePersonEditPage extends StatefulWidget {
  final String uuid;
  final EventPlacePerson eventPlacePerson;
  const EventPlacePersonEditPage({
    super.key,
    required this.uuid,
    required this.eventPlacePerson,
  });

  @override
  State<EventPlacePersonEditPage> createState() =>
      _EventPlacePersonEditPageState();
}

class _EventPlacePersonEditPageState extends State<EventPlacePersonEditPage> {
  final _formKey = GlobalKey<FormState>();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Person> _people = [];
  List<Event> _events = [];
  List<Place> _places = [];

  Person? _selectedPerson;
  Event? _selectedEvent;
  Place? _selectedPlace;

  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
  }

  @override
  void dispose() {
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([
        fetchPeople(widget.uuid),
        fetchEvents(widget.uuid),
        fetchPlaces(widget.uuid),
      ]);
      setState(() {
        _people = results[0] as List<Person>;
        _events = results[1] as List<Event>;
        _places = results[2] as List<Place>;

        _people.sort((a, b) => a.fullName.compareTo(b.fullName));
        _events.sort((a, b) => a.name.compareTo(b.name));
        _places.sort((a, b) => a.name.compareTo(b.name));

        _selectedEvent = _events.firstWhereOrNull(
          (e) => e.id == widget.eventPlacePerson.fkEvent,
        );
        _selectedPlace = _places.firstWhereOrNull(
          (place) => place.id == widget.eventPlacePerson.fkPlace,
        );
        _selectedPerson = _people.firstWhereOrNull(
          (person) => person.id == widget.eventPlacePerson.fkPerson,
        );

        _loading = false;
      });
    } catch (e) {
      setState(() {
        _error = e.toString();
        _loading = false;
      });
    }
  }

  Future<void> _onEventChanged(Event? newEvent) async {
    setState(() {
      _selectedEvent = newEvent;
    });
  }

  Future<void> _onPlaceChanged(Place? newPlace) async {
    setState(() {
      _selectedPlace = newPlace;
    });
  }

  Future<void> _onPersonChanged(Person? newPerson) async {
    setState(() {
      _selectedPerson = newPerson;
    });
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await editEventPlacePerson(
      widget.eventPlacePerson.id,
      _selectedEvent!.id,
      _selectedPlace!.id,
      _selectedPerson!.id,
      widget.uuid,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "EventPlacePerson",
    );
  }

  @override
  Widget build(BuildContext context) {
    if (_loading) {
      return const Center(child: CircularProgressIndicator());
    }
    if (_error != null) {
      return Center(child: Text("Error: $_error"));
    }
    return Scaffold(
      appBar: AppBar(title: Text("Create EventPlacePerson".toUpperCase())),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: SingleChildScrollView(
          child: Center(
            child: Form(
              key: _formKey,
              autovalidateMode: _autoValidate
                  ? AutovalidateMode.onUserInteraction
                  : AutovalidateMode.disabled,
              child: Column(
                children: [
                  EntityDropdown<Event>(
                    label: "Event",
                    selected: _selectedEvent,
                    options: _events,
                    getLabel: (wt) => wt.name,
                    onChanged: _onEventChanged,
                  ),
                  if (_selectedEvent != null)
                    DropdownDescription(_selectedEvent!.description),
                  const SizedBox(height: 16),
                  EntityDropdown(
                    label: "Place",
                    selected: _selectedPlace,
                    options: _places,
                    getLabel: (dt) => dt.name,
                    onChanged: _onPlaceChanged,
                  ),
                  if (_selectedPlace != null)
                    DropdownDescription(_selectedPlace!.description),
                  const SizedBox(height: 16),
                  EntityDropdown(
                    label: "Person",
                    selected: _selectedPerson,
                    options: _people,
                    getLabel: (dice) => dice.name,
                    onChanged: _onPersonChanged,
                  ),
                  if (_selectedPerson != null)
                    DropdownDescription(_selectedPerson!.description ?? ''),
                  const SizedBox(height: 24),
                  SubmitButton(
                    isSubmitting: _isSubmitting,
                    onPressed: _submitForm,
                    label: "Update",
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
