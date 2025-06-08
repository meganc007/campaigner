import 'package:flutter/material.dart';
import 'package:collection/collection.dart';
import 'package:frontend/models/government.dart';
import 'package:frontend/models/location/city.dart';
import 'package:frontend/models/location/country.dart';
import 'package:frontend/models/location/region.dart';
import 'package:frontend/models/location/settlement_type.dart';
import 'package:frontend/models/wealth.dart';
import 'package:frontend/services/locations/city_service.dart';
import 'package:frontend/services/locations/country_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/government_service.dart';
import 'package:frontend/services/locations/region_service.dart';
import 'package:frontend/services/locations/settlement_type_service.dart';
import 'package:frontend/services/wealth_service.dart';
import 'package:frontend/widgets/pages/locations/add/add_region_page.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class CityEditPage extends StatefulWidget {
  final String uuid;
  final City city;
  const CityEditPage({super.key, required this.uuid, required this.city});

  @override
  State<CityEditPage> createState() => _CityEditPageState();
}

class _CityEditPageState extends State<CityEditPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Wealth> _wealth = [];
  List<Country> _countries = [];
  List<SettlementType> _settlementTypes = [];
  List<Government> _governments = [];
  List<Region> _regions = [];
  List<Region> _filteredRegions = [];
  Wealth? _selectedWealth;
  Country? _selectedCountry;
  SettlementType? _selectedSettlementType;
  Government? _selectedGovernment;
  Region? _selectedRegion;
  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
    _nameController.text = widget.city.name;
    _descriptionController.text = widget.city.description;
  }

  @override
  void dispose() {
    _nameController.dispose();
    _descriptionController.dispose();
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([
        fetchWealth(),
        fetchCountries(widget.uuid),
        fetchSettlementTypes(),
        fetchGovernments(),
        fetchRegions(widget.uuid),
      ]);
      setState(() {
        _wealth = results[0] as List<Wealth>;
        _countries = results[1] as List<Country>;
        _settlementTypes = results[2] as List<SettlementType>;
        _governments = results[3] as List<Government>;
        _regions = results[4] as List<Region>;

        _selectedWealth = _wealth.firstWhere(
          (w) => w.id == widget.city.fkWealth,
          orElse: () => _wealth.first,
        );
        _selectedCountry = _countries.firstWhere(
          (country) => country.id == widget.city.fkCountry,
          orElse: () => _countries.first,
        );
        _selectedSettlementType = _settlementTypes.firstWhere(
          (st) => st.id == widget.city.fkSettlement,
          orElse: () => _settlementTypes.first,
        );
        _selectedGovernment = _governments.firstWhere(
          (g) => g.id == widget.city.fkGovernment,
          orElse: () => _governments.first,
        );
        _selectedRegion = _regions.firstWhere(
          (r) =>
              r.id == widget.city.fkRegion &&
              r.fkCountry == _selectedCountry!.id,
          orElse: () => _regions.first,
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

  void _filterRegions() {
    setState(() {
      if (_selectedCountry != null) {
        _filteredRegions = _regions
            .where((region) => region.fkCountry == _selectedCountry!.id)
            .toList();
      } else {
        _filteredRegions = [];
      }
    });
  }

  Future<void> _refreshRegions() async {
    final updatedRegions = await fetchRegions(widget.uuid);
    setState(() {
      _regions = updatedRegions;
      _filterRegions();
    });
  }

  Future<void> _onCountryChanged(Country? newCountry) async {
    setState(() {
      _selectedCountry = newCountry;
      _selectedRegion = null;
      _filterRegions();
    });
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    final localContext = context;

    final success = await editCity(
      widget.uuid,
      widget.city.id,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      _selectedWealth!.id,
      _selectedCountry!.id,
      _selectedSettlementType!.id,
      _selectedGovernment!.id,
      _selectedRegion!.id,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "City",
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
      appBar: AppBar(title: Text("Edit ${widget.city.name}".toUpperCase())),
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
                  EntityDropdown<Wealth>(
                    label: "Wealth",
                    selected: _selectedWealth,
                    options: _wealth,
                    getLabel: (w) => w.name,
                    onChanged: (value) =>
                        setState(() => _selectedWealth = value),
                  ),
                  const SizedBox(height: 16),
                  EntityDropdown<Country>(
                    label: "Country",
                    selected: _selectedCountry,
                    options: _countries,
                    getLabel: (c) => c.name,
                    onChanged: _onCountryChanged,
                  ),
                  const SizedBox(height: 16),
                  if (_selectedCountry != null)
                    DropdownDescription(_selectedCountry!.description),
                  const SizedBox(height: 16),
                  EntityDropdown<SettlementType>(
                    label: "Settlement Type",
                    selected: _selectedSettlementType,
                    options: _settlementTypes,
                    getLabel: (st) => st.name,
                    onChanged: (value) =>
                        setState(() => _selectedSettlementType = value),
                  ),
                  const SizedBox(height: 16),
                  if (_selectedSettlementType != null)
                    DropdownDescription(_selectedSettlementType!.description),
                  const SizedBox(height: 16),
                  EntityDropdown<Government>(
                    label: "Government",
                    selected: _selectedGovernment,
                    options: _governments,
                    getLabel: (g) => g.name,
                    onChanged: (value) =>
                        setState(() => _selectedGovernment = value),
                  ),
                  const SizedBox(height: 16),
                  if (_selectedGovernment != null)
                    DropdownDescription(_selectedGovernment!.description),
                  const SizedBox(height: 16),
                  EntityDropdown<Region>(
                    label: "Region",
                    selected: _selectedRegion,
                    options: _filteredRegions,
                    getLabel: (r) => r.name,
                    onChanged: (value) =>
                        setState(() => _selectedRegion = value),
                  ),
                  const SizedBox(height: 16),
                  if (_selectedCountry != null && _filteredRegions.isEmpty)
                    DropdownDescription(
                      "There are no regions associated with that country.",
                      color: Colors.red,
                      linkText: "Create one?",
                      onLinkTap: () async {
                        await Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (_) => AddRegionPage(
                              uuid: widget.uuid,
                              preselectedCountry: _selectedCountry!.id,
                            ),
                          ),
                        );
                        await _refreshRegions();
                        setState(() {
                          _selectedRegion = _filteredRegions.firstWhereOrNull(
                            (r) => r.id == _selectedRegion?.id,
                          );
                        });
                      },
                    ),
                  if (_selectedRegion != null)
                    DropdownDescription(_selectedRegion!.description),
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
