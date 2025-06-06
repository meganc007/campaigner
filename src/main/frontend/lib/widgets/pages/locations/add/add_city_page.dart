import 'package:flutter/material.dart';
import 'package:frontend/models/government.dart';
import 'package:frontend/models/location/country.dart';
import 'package:frontend/models/location/region.dart';
import 'package:frontend/models/location/settlement_type.dart';
import 'package:frontend/models/wealth.dart';
import 'package:frontend/services/city_service.dart';
import 'package:frontend/services/country_service.dart';
import 'package:frontend/services/government_service.dart';
import 'package:frontend/services/region_service.dart';
import 'package:frontend/services/settlement_type_service.dart';
import 'package:frontend/services/wealth_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddCityPage extends StatefulWidget {
  final String uuid;
  const AddCityPage({super.key, required this.uuid});

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
    super.dispose();
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
                  const SizedBox(height: 24),
                  EntityDropdown<Wealth>(
                    label: "Wealth",
                    selected: _selectedWealth,
                    options: _wealth,
                    getLabel: (w) => w.name,
                    onChanged: (value) =>
                        setState(() => _selectedWealth = value),
                  ),
                  SizedBox(height: 16),
                  EntityDropdown<Country>(
                    label: "Country",
                    selected: _selectedCountry,
                    options: _countries,
                    getLabel: (c) => c.name,
                    onChanged: (value) =>
                        setState(() => _selectedCountry = value),
                  ),
                  SizedBox(height: 16),
                  if (_selectedCountry != null)
                    DropdownDescription(_selectedCountry!.description),
                  SizedBox(height: 16),
                  EntityDropdown<SettlementType>(
                    label: "Settlement Type",
                    selected: _selectedSettlementType,
                    options: _settlementTypes,
                    getLabel: (st) => st.name,
                    onChanged: (value) =>
                        setState(() => _selectedSettlementType = value),
                  ),
                  SizedBox(height: 16),
                  if (_selectedSettlementType != null)
                    DropdownDescription(_selectedSettlementType!.description),
                  SizedBox(height: 16),
                  EntityDropdown<Government>(
                    label: "Government",
                    selected: _selectedGovernment,
                    options: _governments,
                    getLabel: (g) => g.name,
                    onChanged: (value) =>
                        setState(() => _selectedGovernment = value),
                  ),
                  SizedBox(height: 16),
                  if (_selectedGovernment != null)
                    DropdownDescription(_selectedGovernment!.description),
                  SizedBox(height: 16),
                  EntityDropdown<Region>(
                    label: "Region",
                    selected: _selectedRegion,
                    options: _regions,
                    getLabel: (r) => r.name,
                    onChanged: (value) =>
                        setState(() => _selectedRegion = value),
                  ),
                  SizedBox(height: 16),
                  if (_selectedRegion != null)
                    DropdownDescription(_selectedRegion!.description),
                  SizedBox(height: 24),
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
