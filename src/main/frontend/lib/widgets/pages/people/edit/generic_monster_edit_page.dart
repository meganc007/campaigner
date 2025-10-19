import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:frontend/models/people/ability_score.dart';
import 'package:frontend/models/people/generic_monster.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/ability_score_service.dart';
import 'package:frontend/services/people/generic_monster_service.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class GenericMonsterEditPage extends StatefulWidget {
  final String uuid;
  final GenericMonster genericMonster;
  const GenericMonsterEditPage({
    super.key,
    required this.uuid,
    required this.genericMonster,
  });

  @override
  State<GenericMonsterEditPage> createState() => _GenericMonsterEditPageState();
}

class _GenericMonsterEditPageState extends State<GenericMonsterEditPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _traitsController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _notesController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<AbilityScore> _abilityScores = [];

  AbilityScore? _selectedAbilityScore;

  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
    _nameController.text = widget.genericMonster.name;
    _traitsController.text = widget.genericMonster.traits ?? '';
    _descriptionController.text = widget.genericMonster.description ?? '';
    _notesController.text = widget.genericMonster.notes ?? '';
  }

  @override
  void dispose() {
    _nameController.dispose();
    _traitsController.dispose();
    _descriptionController.dispose();
    _notesController.dispose();
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([fetchAbilityScores(widget.uuid)]);
      setState(() {
        _abilityScores = results[0];
        _selectedAbilityScore = _abilityScores.firstWhereOrNull(
          (abs) => abs.id == widget.genericMonster.fkAbilityScore,
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

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final description = _descriptionController.text.trim();
    final traits = _traitsController.text.trim();
    final notes = _notesController.text.trim();

    final success = await editGenericMonster(
      widget.genericMonster.id,
      _nameController.text.trim(),
      description.isNotEmpty ? description : null,
      _selectedAbilityScore?.id,
      traits.isNotEmpty ? traits : null,
      notes.isNotEmpty ? notes : null,
      widget.uuid,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Generic Monster",
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
        title: Text("Edit ${widget.genericMonster.name}".toUpperCase()),
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
                  EntityDropdown<AbilityScore>(
                    label: "Ability Score",
                    selected: _selectedAbilityScore,
                    options: _abilityScores,
                    getLabel: (abilityScore) => abilityScore.toShortString(),
                    isOptional: true,
                    onChanged: (value) =>
                        setState(() => _selectedAbilityScore = value),
                  ),
                  const SizedBox(height: 16),
                  if (_selectedAbilityScore != null)
                    DropdownDescription(_selectedAbilityScore!.statsOnly()),
                  const SizedBox(height: 16),
                  StyledTextField(
                    controller: _traitsController,
                    label: "Traits",
                    maxLines: 3,
                  ),
                  const SizedBox(height: 12),
                  StyledTextField(
                    controller: _descriptionController,
                    label: "Description",
                    maxLines: 3,
                  ),
                  const SizedBox(height: 12),
                  StyledTextField(
                    controller: _notesController,
                    label: "Notes",
                    maxLines: 3,
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
