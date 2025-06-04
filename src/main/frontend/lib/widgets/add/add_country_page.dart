import 'package:flutter/material.dart';
import 'package:frontend/models/government.dart';
import 'package:frontend/models/location/continent.dart';
import 'package:frontend/services/continent_service.dart';
import 'package:frontend/services/country_service.dart';
import 'package:frontend/services/government_service.dart';
import 'package:frontend/widgets/dropdown_description.dart';
import 'package:frontend/widgets/styled_dropdown.dart';
import 'package:frontend/widgets/styled_text_field.dart';
import 'package:frontend/widgets/submit_button.dart';

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

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([
        fetchGovernments(),
        fetchContinents(widget.uuid),
      ]);
      setState(() {
        _governments = results[0] as List<Government>;
        _continents = results[1] as List<Continent>;
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

    if (mounted && success) {
      ScaffoldMessenger.of(localContext).showSnackBar(
        const SnackBar(content: Text("Country created successfully!")),
      );
      Navigator.pop(localContext, true);
    } else {
      ScaffoldMessenger.of(localContext).showSnackBar(
        const SnackBar(content: Text("Failed to create country.")),
      );
    }
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
              child: Column(
                children: [
                  StyledTextField(
                    controller: _nameController,
                    label: "Name",
                    validator: (value) =>
                        value == null || value.isEmpty ? 'Name required' : null,
                  ),
                  const SizedBox(height: 12),
                  StyledTextField(
                    controller: _descriptionController,
                    label: "Description",
                    maxLines: 3,
                  ),
                  const SizedBox(height: 24),
                  StyledDropdown<Continent>(
                    label: "Continent",
                    value: _selectedContinent,
                    items: _continents.map((continent) {
                      return DropdownMenuItem<Continent>(
                        value: continent,
                        child: Text(continent.name),
                      );
                    }).toList(),
                    onChanged: (value) {
                      setState(() => _selectedContinent = value);
                    },
                    validator: (value) =>
                        value == null ? "Please select a continent" : null,
                  ),
                  SizedBox(height: 16),
                  if (_selectedContinent != null)
                    DropdownDescription(_selectedContinent!.description),
                  SizedBox(height: 16),
                  StyledDropdown<Government>(
                    label: "Government",
                    value: _selectedGovernment,
                    items: _governments.map((gov) {
                      return DropdownMenuItem<Government>(
                        value: gov,
                        child: Text(gov.name),
                      );
                    }).toList(),
                    onChanged: (value) {
                      setState(() => _selectedGovernment = value);
                    },
                    validator: (value) =>
                        value == null ? "Please select a government" : null,
                  ),
                  SizedBox(height: 16),
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
