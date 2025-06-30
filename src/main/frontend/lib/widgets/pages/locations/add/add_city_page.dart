import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:frontend/models/government.dart';
import 'package:frontend/models/location/country.dart';
import 'package:frontend/models/location/region.dart';
import 'package:frontend/models/location/settlement_type.dart';
import 'package:frontend/models/wealth.dart';
import 'package:frontend/services/locations/city_service.dart';
import 'package:frontend/services/locations/country_service.dart';
import 'package:frontend/services/government_service.dart';
import 'package:frontend/services/locations/region_service.dart';
import 'package:frontend/services/locations/settlement_type_service.dart';
import 'package:frontend/services/wealth_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/pages/locations/add/add_country_page.dart';
import 'package:frontend/widgets/pages/locations/add/add_region_page.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/no_associated_entity.dart';
import 'package:frontend/widgets/reusable/no_parent_entity.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddCityPage extends StatefulWidget {
  final String uuid;
  final int? preselectedCountry;
  const AddCityPage({super.key, required this.uuid, this.preselectedCountry});

  @override
  State<AddCityPage> createState() => _AddCityPageState();
}

class _AddCityPageState extends State<AddCityPage> {
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

        if (_countries.length == 1) {
          _selectedCountry = _countries.first;
          _filterRegions();
        }

        if (widget.preselectedCountry != null) {
          _selectedCountry = _countries.firstWhereOrNull(
            (country) => country.id == widget.preselectedCountry,
          );
          _filterRegions();
        }
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
        if (_filteredRegions.length == 1) {
          _selectedRegion = _filteredRegions.first;
        }
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

  Future<void> _refreshCountries() async {
    final updatedCountries = await fetchCountries(widget.uuid);
    setState(() {
      _countries = updatedCountries;
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

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await createCity(
      widget.uuid,
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

    handleSuccessOrFailureOnCreate(
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
      appBar: AppBar(title: Text("Create City".toUpperCase())),
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
                  const SizedBox(height: 12),
                  EntityDropdown<Country>(
                    label: "Country",
                    selected: _selectedCountry,
                    options: _countries,
                    getLabel: (c) => c.name,
                    onChanged: _onCountryChanged,
                  ),
                  const SizedBox(height: 16),
                  NoParentEntity(
                    parents: "countries",
                    show: _countries.isEmpty,
                    onCreateTap: (context) async {
                      final bool? didCreate = await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => AddCountryPage(uuid: widget.uuid),
                        ),
                      );
                      if (didCreate == true) {
                        await _refreshCountries();
                      }
                    },
                  ),
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
                  NoParentEntity(
                    parents: "regions",
                    show: _regions.isEmpty && _selectedCountry == null,
                    onCreateTap: (context) async {
                      final bool? didCreate = await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => AddRegionPage(uuid: widget.uuid),
                        ),
                      );
                      if (didCreate == true) {
                        await _refreshRegions();
                      }
                    },
                  ),
                  NoAssociatedEntity(
                    show: _selectedCountry != null && _filteredRegions.isEmpty,
                    children: "regions",
                    parent: "country",
                    onCreateTap: (context) async {
                      final bool? didCreate = await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => AddRegionPage(
                            uuid: widget.uuid,
                            preselectedCountry: _selectedCountry!.id,
                          ),
                        ),
                      );
                      if (didCreate == true) {
                        await _refreshRegions();
                      }
                    },
                  ),
                  if (_selectedRegion != null)
                    DropdownDescription(_selectedRegion!.description),
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
