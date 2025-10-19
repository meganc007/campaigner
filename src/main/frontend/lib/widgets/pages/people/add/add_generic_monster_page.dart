import 'package:flutter/material.dart';
import 'package:frontend/models/people/ability_score.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/ability_score_service.dart';
import 'package:frontend/services/people/generic_monster_service.dart';
import 'package:frontend/widgets/pages/people/add/add_ability_score_page.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/no_parent_entity.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddGenericMonsterPage extends StatefulWidget {
  final String uuid;
  const AddGenericMonsterPage({super.key, required this.uuid});

  @override
  State<AddGenericMonsterPage> createState() => _AddGenericMonsterPageState();
}

class _AddGenericMonsterPageState extends State<AddGenericMonsterPage> {
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
        _loading = false;
      });
    } catch (e) {
      setState(() {
        _error = e.toString();
        _loading = false;
      });
    }
  }

  Future<void> _refreshAbilityScores() async {
    final updatedAbilityScores = await fetchAbilityScores(widget.uuid);

    setState(() {
      _abilityScores = updatedAbilityScores;

      if (_abilityScores.isNotEmpty) {
        _selectedAbilityScore = _abilityScores.first;
      }
    });
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await createGenericMonster(
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      _selectedAbilityScore?.id,
      _traitsController.text.trim(),
      _notesController.text.trim(),
      widget.uuid,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnCreate(
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
      appBar: AppBar(title: Text("Create Generic Monster".toUpperCase())),
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
                  NoParentEntity(
                    show: _abilityScores.isEmpty,
                    parents: "ability scores",
                    onCreateTap: (context) async {
                      final bool? didCreate = await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              AddAbilityScorePage(uuid: widget.uuid),
                        ),
                      );
                      if (didCreate == true) {
                        await _refreshAbilityScores();
                      }
                    },
                  ),
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
