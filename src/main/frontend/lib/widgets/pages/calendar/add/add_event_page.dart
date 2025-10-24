import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/models/calendar/day.dart';
import 'package:frontend/models/calendar/month.dart';
import 'package:frontend/models/calendar/week.dart';
import 'package:frontend/models/location/city.dart';
import 'package:frontend/models/location/continent.dart';
import 'package:frontend/models/location/country.dart';
import 'package:frontend/services/calendar/day_service.dart';
import 'package:frontend/services/calendar/event_service.dart';
import 'package:frontend/services/calendar/month_service.dart';
import 'package:frontend/services/calendar/week_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/locations/city_service.dart';
import 'package:frontend/services/locations/continent_service.dart';
import 'package:frontend/services/locations/country_service.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddEventPage extends StatefulWidget {
  final String uuid;
  const AddEventPage({super.key, required this.uuid});

  @override
  State<AddEventPage> createState() => _AddEventPageState();
}

class _AddEventPageState extends State<AddEventPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _eventYearController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Month> _months = [];
  List<Week> _weeks = [];
  List<Day> _days = [];
  List<Continent> _continents = [];
  List<Country> _countries = [];
  List<Country> _filteredCountries = [];
  List<City> _cities = [];
  List<City> _filteredCities = [];

  Month? _selectedMonth;
  Week? _selectedWeek;
  Day? _selectedDay;
  Continent? _selectedContinent;
  Country? _selectedCountry;
  City? _selectedCity;

  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
  }

  @override
  void dispose() {
    _nameController.dispose();
    _descriptionController.dispose();
    _eventYearController.dispose();
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([
        fetchMonths(widget.uuid),
        fetchWeeks(widget.uuid),
        fetchDays(widget.uuid),
        fetchContinents(widget.uuid),
        fetchCountries(widget.uuid),
        fetchCities(widget.uuid),
      ]);
      setState(() {
        _months = results[0] as List<Month>;
        _weeks = results[1] as List<Week>;
        _days = results[2] as List<Day>;
        _continents = results[3] as List<Continent>;
        _countries = results[4] as List<Country>;
        _cities = results[5] as List<City>;
        _loading = false;
      });
    } catch (e) {
      setState(() {
        _error = e.toString();
        _loading = false;
      });
    }
  }

  void _filterCountries() {
    setState(() {
      if (_selectedContinent != null) {
        _filteredCountries = _countries
            .where((country) => country.fkContinent == _selectedContinent!.id)
            .toList();
        if (_filteredCountries.length == 1) {
          _selectedCountry = _filteredCountries.first;
        }
      } else {
        _filteredCountries = [];
      }
    });
  }

  void _filterCities() {
    setState(() {
      if (_selectedCountry != null) {
        _filteredCities = _cities
            .where((city) => city.fkCountry == _selectedCountry!.id)
            .toList();
        if (_filteredCities.length == 1) {
          _selectedCity = _filteredCities.first;
        }
      } else {
        _filteredCities = [];
      }
    });
  }

  Future<void> _onContinentChanged(Continent? newContinent) async {
    setState(() {
      _selectedContinent = newContinent;
      _selectedCountry = null;
      _filterCountries();
    });
  }

  Future<void> _onCountryChanged(Country? newCountry) async {
    setState(() {
      _selectedCountry = newCountry;
      _selectedCity = null;
      _filterCities();
    });
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await createEvent(
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      int.parse(_eventYearController.text.trim()),
      _selectedMonth!.id,
      _selectedWeek!.id,
      _selectedDay!.id,
      _selectedContinent!.id,
      _selectedCountry!.id,
      _selectedCity!.id,
      widget.uuid,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnCreate(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Event",
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
      appBar: AppBar(title: Text("Create Event".toUpperCase())),
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
                  const SizedBox(height: 12),
                  EntityDropdown<Month>(
                    label: "Month",
                    selected: _selectedMonth,
                    options: _months,
                    getLabel: (month) => month.name,
                    onChanged: (value) =>
                        setState(() => _selectedMonth = value),
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
                    onChanged: (value) => setState(() => _selectedWeek = value),
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
                    onChanged: (value) => setState(() => _selectedDay = value),
                  ),
                  if (_selectedDay != null)
                    DropdownDescription(_selectedDay!.description),
                  SizedBox(height: _selectedDay?.description != null ? 16 : 0),
                  const SizedBox(height: 16),
                  EntityDropdown<Continent>(
                    label: "Continent",
                    selected: _selectedContinent,
                    options: _continents,
                    getLabel: (continent) => continent.name,
                    onChanged: _onContinentChanged,
                  ),
                  if (_selectedContinent != null)
                    DropdownDescription(_selectedContinent!.description),
                  SizedBox(
                    height: _selectedContinent?.description != null ? 16 : 0,
                  ),
                  const SizedBox(height: 16),
                  EntityDropdown<Country>(
                    label: "Country",
                    selected: _selectedCountry,
                    options: _filteredCountries,
                    getLabel: (country) => country.name,
                    onChanged: _onCountryChanged,
                  ),
                  if (_selectedCountry != null)
                    DropdownDescription(_selectedCountry!.description),
                  SizedBox(
                    height: _selectedCountry?.description != null ? 16 : 0,
                  ),
                  const SizedBox(height: 16),
                  EntityDropdown<City>(
                    label: "City",
                    selected: _selectedCity,
                    options: _filteredCities,
                    getLabel: (city) => city.name,
                    onChanged: (value) => setState(() => _selectedCity = value),
                  ),
                  if (_selectedCity != null)
                    DropdownDescription(_selectedCity!.description),
                  SizedBox(height: _selectedCity?.description != null ? 16 : 0),
                  const SizedBox(height: 24),
                  SubmitButton(
                    isSubmitting: _isSubmitting,
                    onPressed: _submitForm,
                    label: "Create",
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
