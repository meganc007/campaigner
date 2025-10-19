import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:frontend/models/people/ability_score.dart';
import 'package:frontend/models/people/generic_monster.dart';
import 'package:frontend/models/people/named_monster.dart';
import 'package:frontend/models/wealth.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/ability_score_service.dart';
import 'package:frontend/services/people/generic_monster_service.dart';
import 'package:frontend/services/people/named_monster_service.dart';
import 'package:frontend/services/wealth_service.dart';
import 'package:frontend/widgets/reusable/dropdown_boolean.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class NamedMonsterEditPage extends StatefulWidget {
  final String uuid;
  final NamedMonster namedMonster;
  const NamedMonsterEditPage({
    super.key,
    required this.uuid,
    required this.namedMonster,
  });

  @override
  State<NamedMonsterEditPage> createState() => _NamedMonsterEditPageState();
}

class _NamedMonsterEditPageState extends State<NamedMonsterEditPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _firstNameController = TextEditingController();
  final TextEditingController _lastNameController = TextEditingController();
  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _personalityController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _notesController = TextEditingController();
  bool? _isEnemy;
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Wealth> _wealth = [];
  List<AbilityScore> _abilityScores = [];
  List<GenericMonster> _genericMonsters = [];

  Wealth? _selectedWealth;
  AbilityScore? _selectedAbilityScore;
  GenericMonster? _selectedGenericMonster;
  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
    _firstNameController.text = widget.namedMonster.firstName;
    _lastNameController.text = widget.namedMonster.lastName ?? '';
    _titleController.text = widget.namedMonster.title ?? '';
    _personalityController.text = widget.namedMonster.personality;
    _descriptionController.text = widget.namedMonster.description;
    _notesController.text = widget.namedMonster.notes ?? '';
    _isEnemy = widget.namedMonster.isEnemy!;
  }

  @override
  void dispose() {
    _firstNameController.dispose();
    _lastNameController.dispose();
    _titleController.dispose();
    _personalityController.dispose();
    _descriptionController.dispose();
    _notesController.dispose();
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([
        fetchWealth(),
        fetchAbilityScores(widget.uuid),
        fetchGenericMonsters(widget.uuid),
      ]);
      setState(() {
        _wealth = results[0] as List<Wealth>;
        _abilityScores = results[1] as List<AbilityScore>;
        _genericMonsters = results[2] as List<GenericMonster>;

        _selectedWealth = _wealth.firstWhereOrNull(
          (w) => w.id == widget.namedMonster.fkWealth,
        );
        _selectedAbilityScore = _abilityScores.firstWhereOrNull(
          (abs) => abs.id == widget.namedMonster.fkAbilityScore,
        );
        _selectedGenericMonster = _genericMonsters.firstWhereOrNull(
          (gm) => gm.id == widget.namedMonster.fkGenericMonster,
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

  Future<void> _onWealthChanged(Wealth? newWealth) async {
    setState(() {
      _selectedWealth = newWealth;
    });
  }

  Future<void> _onAbilityScoreChanged(AbilityScore? newAbilityScore) async {
    setState(() {
      _selectedAbilityScore = newAbilityScore;
    });
  }

  Future<void> _onGenericMonsterChanged(
    GenericMonster? newGenericMonster,
  ) async {
    setState(() {
      _selectedGenericMonster = newGenericMonster;
    });
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    final localContext = context;

    final lastName = _lastNameController.text.trim();
    final title = _titleController.text.trim();
    final notes = _notesController.text.trim();

    final success = await editNamedMonster(
      widget.namedMonster.id,
      _firstNameController.text.trim(),
      lastName.isNotEmpty ? lastName : null,
      title.isNotEmpty ? title : null,
      _selectedWealth!.id,
      _selectedAbilityScore!.id,
      _selectedGenericMonster!.id,
      _isEnemy,
      _personalityController.text.trim(),
      _descriptionController.text.trim(),
      notes.isNotEmpty ? notes : null,
      widget.uuid,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Named Monster",
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
        title: Text("Edit ${widget.namedMonster.name}".toUpperCase()),
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
                    controller: _firstNameController,
                    label: "First Name",
                    validator: isNameValid,
                  ),
                  const SizedBox(height: 12),
                  StyledTextField(
                    controller: _lastNameController,
                    label: "Last Name",
                    validator: isNameValid,
                  ),
                  const SizedBox(height: 12),
                  StyledTextField(controller: _titleController, label: "Title"),
                  const SizedBox(height: 12),
                  EntityDropdown<Wealth>(
                    label: "Wealth",
                    selected: _selectedWealth,
                    options: _wealth,
                    getLabel: (w) => w.name,
                    onChanged: _onWealthChanged,
                  ),
                  const SizedBox(height: 12),
                  EntityDropdown<AbilityScore>(
                    label: "Ability Score",
                    selected: _selectedAbilityScore,
                    options: _abilityScores,
                    getLabel: (abs) => abs.toShortString(),
                    onChanged: _onAbilityScoreChanged,
                  ),
                  const SizedBox(height: 12),
                  EntityDropdown<GenericMonster>(
                    label: "Generic Monster",
                    selected: _selectedGenericMonster,
                    options: _genericMonsters,
                    getLabel: (gm) => gm.name,
                    onChanged: _onGenericMonsterChanged,
                  ),
                  if (_selectedGenericMonster != null)
                    DropdownDescription(
                      _selectedGenericMonster!.description ?? '',
                    ),
                  SizedBox(
                    height: _selectedGenericMonster?.description != null
                        ? 16
                        : 0,
                  ),
                  BooleanDropdown(
                    label: "Is Enemy?",
                    selected: _isEnemy,
                    onChanged: (value) {
                      setState(() {
                        _isEnemy = value;
                      });
                    },
                    isOptional: false,
                  ),
                  const SizedBox(height: 12),
                  StyledTextField(
                    controller: _personalityController,
                    label: "Personality",
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
