import 'package:flutter/material.dart';
import 'package:frontend/models/climate.dart';
import 'package:frontend/models/location/country.dart';
import 'package:frontend/services/climate_service.dart';
import 'package:frontend/services/country_service.dart';
import 'package:frontend/services/region_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/dropdown_description.dart';
import 'package:frontend/widgets/entity_dropdown.dart';
import 'package:frontend/widgets/styled_text_field.dart';
import 'package:frontend/widgets/submit_button.dart';

class AddRegionPage extends StatefulWidget {
  final String uuid;
  const AddRegionPage({super.key, required this.uuid});

  @override
  State<AddRegionPage> createState() => _AddRegionPageState();
}

class _AddRegionPageState extends State<AddRegionPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Country> _countries = [];
  List<Climate> _climates = [];
  Country? _selectedCountry;
  Climate? _selectedClimate;
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
        fetchCountries(widget.uuid),
        fetchClimates(),
      ]);
      setState(() {
        _countries = results[0] as List<Country>;
        _climates = results[1] as List<Climate>;
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

    final success = await createRegion(
      widget.uuid,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      _selectedCountry!.id,
      _selectedClimate!.id,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnCreate(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Region",
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
      appBar: AppBar(title: Text("Create Region".toUpperCase())),
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
                  EntityDropdown<Climate>(
                    label: "Climate",
                    selected: _selectedClimate,
                    options: _climates,
                    getLabel: (c) => c.name,
                    onChanged: (value) =>
                        setState(() => _selectedClimate = value),
                  ),
                  SizedBox(height: 16),
                  if (_selectedClimate != null)
                    DropdownDescription(_selectedClimate!.description),
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
