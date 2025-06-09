import 'package:flutter/material.dart';
import 'package:frontend/models/location/region.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/locations/landmark_service.dart';
import 'package:frontend/services/locations/region_service.dart';
import 'package:frontend/widgets/pages/locations/add/add_region_page.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/no_parent_entity.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddLandmarkPage extends StatefulWidget {
  final String uuid;
  const AddLandmarkPage({super.key, required this.uuid});

  @override
  State<AddLandmarkPage> createState() => _AddLandmarkPageState();
}

class _AddLandmarkPageState extends State<AddLandmarkPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _isSubmitting = false;
  final bool _autoValidate = false;

  List<Region> _regions = [];
  Region? _selectedRegion;
  bool _isLoading = true;
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
      final results = await Future.wait([fetchRegions(widget.uuid)]);
      setState(() {
        _regions = results[0];
        if (_regions.length == 1) {
          _selectedRegion = _regions.first;
        }
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _error = e.toString();
        _isLoading = false;
      });
    }
  }

  Future<void> _refreshRegions() async {
    final updatedRegions = await fetchRegions(widget.uuid);
    setState(() {
      _regions = updatedRegions;
      if (_regions.length == 1) {
        _selectedRegion = _regions.first;
      }
    });
  }

  Future<void> _submitForm() async {
    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await createLandmark(
      widget.uuid,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      _selectedRegion!.id,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnCreate(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Landmark",
    );
  }

  @override
  Widget build(BuildContext context) {
    if (_isLoading) {
      return const Center(child: CircularProgressIndicator());
    }
    if (_error != null) {
      return Center(child: Text("Error: $_error"));
    }
    return Scaffold(
      appBar: AppBar(title: Text("Create Landmark".toUpperCase())),
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
                  EntityDropdown<Region>(
                    label: "Region",
                    selected: _selectedRegion,
                    options: _regions,
                    getLabel: (r) => r.name,
                    onChanged: (value) =>
                        setState(() => _selectedRegion = value),
                  ),
                  const SizedBox(height: 16),
                  NoParentEntity(
                    parents: "regions",
                    show: _regions.isEmpty,
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
