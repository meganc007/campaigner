import 'package:flutter/material.dart';
import 'package:frontend/models/government.dart';
import 'package:frontend/models/location/continent.dart';
import 'package:frontend/services/locations/continent_service.dart';
import 'package:frontend/services/locations/country_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/government_service.dart';
import 'package:frontend/widgets/pages/locations/add/add_continent_page.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/missing_parent.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddCountryPage extends StatefulWidget {
  final String uuid;
  const AddCountryPage({super.key, required this.uuid});

  @override
  State<AddCountryPage> createState() => _AddCountryPageState();
}

class _AddCountryPageState extends State<AddCountryPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Continent> _continents = [];
  List<Government> _governments = [];
  Government? _selectedGovernment;
  Continent? _selectedContinent;
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
        fetchGovernments(),
        fetchContinents(widget.uuid),
      ]);
      setState(() {
        _governments = results[0] as List<Government>;
        _continents = results[1] as List<Continent>;

        if (_continents.length == 1) {
          _selectedContinent = _continents.first;
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

  Future<void> _refreshContinents() async {
    final updatedContinents = await fetchContinents(widget.uuid);
    setState(() {
      _continents = updatedContinents;
    });
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await createCountry(
      widget.uuid,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      _selectedContinent!.id,
      _selectedGovernment!.id,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnCreate(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Country",
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
      appBar: AppBar(title: Text("Create Country".toUpperCase())),
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
                  EntityDropdown<Continent>(
                    label: "Continent",
                    selected: _selectedContinent,
                    options: _continents,
                    getLabel: (c) => c.name,
                    onChanged: (value) =>
                        setState(() => _selectedContinent = value),
                  ),
                  const SizedBox(height: 16),
                  MissingParent(
                    show: _continents.isEmpty,
                    parents: "continents",
                    onCreateTap: (context) async {
                      await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => AddContinentPage(uuid: widget.uuid),
                        ),
                      );
                      await _refreshContinents();
                      if (_continents.isNotEmpty) {
                        setState(() => _selectedContinent = _continents.first);
                      }
                    },
                  ),
                  if (_selectedContinent != null)
                    DropdownDescription(_selectedContinent!.description),
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
