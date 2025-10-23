import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/models/calendar/celestial_event.dart';
import 'package:frontend/models/calendar/day.dart';
import 'package:frontend/models/calendar/month.dart';
import 'package:frontend/models/calendar/moon.dart';
import 'package:frontend/models/calendar/sun.dart';
import 'package:frontend/models/calendar/week.dart';
import 'package:frontend/services/calendar/celestial_event_service.dart';
import 'package:frontend/services/calendar/day_service.dart';
import 'package:frontend/services/calendar/month_service.dart';
import 'package:frontend/services/calendar/moon_service.dart';
import 'package:frontend/services/calendar/sun_service.dart';
import 'package:frontend/services/calendar/week_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class CelestialEventEditPage extends StatefulWidget {
  final String uuid;
  final CelestialEvent celestialEvent;
  const CelestialEventEditPage({
    super.key,
    required this.uuid,
    required this.celestialEvent,
  });

  @override
  State<CelestialEventEditPage> createState() => _CelestialEventEditPageState();
}

class _CelestialEventEditPageState extends State<CelestialEventEditPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _eventYearController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Moon> _moons = [];
  List<Sun> _suns = [];
  List<Month> _months = [];
  List<Week> _weeks = [];
  List<Day> _days = [];

  Moon? _selectedMoon;
  Sun? _selectedSun;
  Month? _selectedMonth;
  Week? _selectedWeek;
  Day? _selectedDay;

  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
    _nameController.text = widget.celestialEvent.name;
    _descriptionController.text = widget.celestialEvent.description;
    _eventYearController.text = widget.celestialEvent.eventYear.toString();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([
        fetchMoons(widget.uuid),
        fetchSuns(widget.uuid),
        fetchMonths(widget.uuid),
        fetchWeeks(widget.uuid),
        fetchDays(widget.uuid),
      ]);
      setState(() {
        _moons = results[0] as List<Moon>;
        _suns = results[1] as List<Sun>;
        _months = results[2] as List<Month>;
        _weeks = results[3] as List<Week>;
        _days = results[4] as List<Day>;

        _selectedMoon = _moons.firstWhereOrNull(
          (moon) => moon.id == widget.celestialEvent.fkMoon,
        );
        _selectedSun = _suns.firstWhereOrNull(
          (s) => s.id == widget.celestialEvent.fkSun,
        );
        _selectedMonth = _months.firstWhereOrNull(
          (month) => month.id == widget.celestialEvent.fkMonth,
        );
        _selectedWeek = _weeks.firstWhereOrNull(
          (w) => w.id == widget.celestialEvent.fkWeek,
        );
        _selectedDay = _days.firstWhereOrNull(
          (d) => d.id == widget.celestialEvent.fkDay,
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

  @override
  void dispose() {
    _nameController.dispose();
    _descriptionController.dispose();
    _eventYearController.dispose();
    super.dispose();
  }

  Future<void> _onMoonChanged(Moon? newMoon) async {
    setState(() {
      _selectedMoon = newMoon;
    });
  }

  Future<void> _onSunChanged(Sun? newSun) async {
    setState(() {
      _selectedSun = newSun;
    });
  }

  Future<void> _onMonthChanged(Month? newMonth) async {
    setState(() {
      _selectedMonth = newMonth;
    });
  }

  Future<void> _onWeekChanged(Week? newWeek) async {
    setState(() {
      _selectedWeek = newWeek;
    });
  }

  Future<void> _onDayChanged(Day? newDay) async {
    setState(() {
      _selectedDay = newDay;
    });
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await editCelestialEvent(
      widget.celestialEvent.id,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      widget.uuid,
      _selectedMoon!.id,
      _selectedSun!.id,
      _selectedMonth!.id,
      _selectedWeek!.id,
      _selectedDay!.id,
      int.parse(_eventYearController.text.trim()),
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Celestial Event",
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
      appBar: AppBar(
        title: Text("Edit ${widget.celestialEvent.name}".toUpperCase()),
      ),
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
                  StyledTextField(
                    controller: _nameController,
                    label: "Name",
                    validator: isNameValid,
                  ),
                  const SizedBox(height: 12),
                  StyledTextField(
                    controller: _descriptionController,
                    label: "Description",
                    maxLines: 3,
                  ),
                  const SizedBox(height: 12),
                  EntityDropdown<Moon>(
                    label: "Moon",
                    selected: _selectedMoon,
                    options: _moons,
                    getLabel: (moon) => moon.name,
                    onChanged: _onMoonChanged,
                  ),
                  if (_selectedMoon != null)
                    DropdownDescription(_selectedMoon!.description),
                  SizedBox(height: _selectedMoon?.description != null ? 16 : 0),
                  const SizedBox(height: 16),
                  EntityDropdown<Sun>(
                    label: "Sun",
                    selected: _selectedSun,
                    options: _suns,
                    getLabel: (s) => s.name,
                    onChanged: _onSunChanged,
                  ),
                  if (_selectedSun != null)
                    DropdownDescription(_selectedSun!.description),
                  SizedBox(height: _selectedSun?.description != null ? 16 : 0),
                  const SizedBox(height: 16),
                  EntityDropdown<Month>(
                    label: "Month",
                    selected: _selectedMonth,
                    options: _months,
                    getLabel: (month) => month.name,
                    onChanged: _onMonthChanged,
                  ),
                  if (_selectedMonth != null)
                    DropdownDescription(_selectedMonth!.description),
                  SizedBox(
                    height: _selectedMonth?.description != null ? 16 : 0,
                  ),
                  const SizedBox(height: 16),
                  EntityDropdown<Week>(
                    label: "Week",
                    selected: _selectedWeek,
                    options: _weeks,
                    getLabel: (w) => w.weekNumber.toString(),
                    onChanged: _onWeekChanged,
                  ),
                  if (_selectedWeek != null)
                    DropdownDescription(_selectedWeek!.description),
                  SizedBox(height: _selectedWeek?.description != null ? 16 : 0),
                  const SizedBox(height: 16),
                  EntityDropdown<Day>(
                    label: "Day",
                    selected: _selectedDay,
                    options: _days,
                    getLabel: (d) => d.name,
                    onChanged: _onDayChanged,
                  ),
                  if (_selectedDay != null)
                    DropdownDescription(_selectedDay!.description),
                  SizedBox(height: _selectedDay?.description != null ? 16 : 0),
                  const SizedBox(height: 16),
                  StyledTextField(
                    controller: _eventYearController,
                    label: "Event Year",
                    keyboardType: TextInputType.number,
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    validator: (value) {
                      if (value == null || value.isEmpty) return 'Required';
                      if (int.tryParse(value) == null) {
                        return 'Enter a valid number';
                      }
                      return null;
                    },
                  ),
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
