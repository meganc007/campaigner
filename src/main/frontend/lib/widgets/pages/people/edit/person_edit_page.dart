import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/models/people/ability_score.dart';
import 'package:frontend/models/people/person.dart';
import 'package:frontend/models/people/race.dart';
import 'package:frontend/models/common/wealth.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/ability_score_service.dart';
import 'package:frontend/services/people/person_service.dart';
import 'package:frontend/services/people/race_service.dart';
import 'package:frontend/services/common/wealth_service.dart';
import 'package:frontend/widgets/reusable/dropdown_boolean.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class PersonEditPage extends StatefulWidget {
  final String uuid;
  final Person person;
  const PersonEditPage({super.key, required this.uuid, required this.person});

  @override
  State<PersonEditPage> createState() => _PersonEditPageState();
}

class _PersonEditPageState extends State<PersonEditPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _firstNameController = TextEditingController();
  final TextEditingController _lastNameController = TextEditingController();
  final TextEditingController _ageController = TextEditingController();
  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _personalityController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _notesController = TextEditingController();
  bool? _isNPC;
  bool? _isEnemy;
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Race> _race = [];
  List<Wealth> _wealth = [];
  List<AbilityScore> _abilityScores = [];

  Race? _selectedRace;
  Wealth? _selectedWealth;
  AbilityScore? _selectedAbilityScore;
  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
    _firstNameController.text = widget.person.firstName;
    _lastNameController.text = widget.person.lastName ?? '';
    _ageController.text = widget.person.age.toString();
    _titleController.text = widget.person.title ?? '';
    _personalityController.text = widget.person.personality ?? '';
    _descriptionController.text = widget.person.description ?? '';
    _notesController.text = widget.person.notes ?? '';
    _isNPC = widget.person.isNpc;
    _isEnemy = widget.person.isEnemy;
  }

  @override
  void dispose() {
    _firstNameController.dispose();
    _lastNameController.dispose();
    _ageController.dispose();
    _titleController.dispose();
    _personalityController.dispose();
    _descriptionController.dispose();
    _notesController.dispose();
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([
        fetchRaces(),
        fetchWealth(),
        fetchAbilityScores(widget.uuid),
      ]);
      setState(() {
        _race = results[0] as List<Race>;
        _wealth = results[1] as List<Wealth>;
        _abilityScores = results[2] as List<AbilityScore>;

        _selectedRace = _race.firstWhereOrNull(
          (r) => r.id == widget.person.fkRace,
        );
        _selectedWealth = _wealth.firstWhereOrNull(
          (w) => w.id == widget.person.fkWealth,
        );
        _selectedAbilityScore = _abilityScores.firstWhereOrNull(
          (abs) => abs.id == widget.person.fkAbilityScore,
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

  Future<void> _onRaceChanged(Race? newRace) async {
    setState(() {
      _selectedRace = newRace;
    });
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

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    final localContext = context;

    final lastName = _lastNameController.text.trim();
    final title = _titleController.text.trim();
    final personality = _personalityController.text.trim();
    final description = _descriptionController.text.trim();
    final notes = _notesController.text.trim();

    final success = await editPerson(
      widget.person.id,
      _firstNameController.text.trim(),
      lastName.isNotEmpty ? lastName : null,
      int.tryParse(_ageController.text.trim()),
      title.isNotEmpty ? title : null,
      _selectedRace!.id,
      _selectedWealth!.id,
      _selectedAbilityScore!.id,
      _isNPC!,
      _isEnemy!,
      personality.isNotEmpty ? personality : null,
      description.isNotEmpty ? description : null,
      notes.isNotEmpty ? notes : null,
      widget.uuid,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Person",
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
      appBar: AppBar(title: Text("Edit ${widget.person.name}".toUpperCase())),
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
                  ),
                  const SizedBox(height: 12),
                  StyledTextField(
                    controller: _ageController,
                    label: "Age",
                    keyboardType: TextInputType.number,
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    validator: (value) {
                      if (value == null || value.isEmpty) return 'Required';
                      if (int.tryParse(value) == null) {
                        return 'Enter a valid number';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 12),
                  StyledTextField(controller: _titleController, label: "Title"),
                  const SizedBox(height: 12),
                  EntityDropdown<Race>(
                    label: "Race",
                    selected: _selectedRace,
                    options: _race,
                    getLabel: (r) => r.name,
                    onChanged: _onRaceChanged,
                  ),
                  if (_selectedRace != null)
                    DropdownDescription(_selectedRace!.description),
                  SizedBox(height: _selectedRace?.description != null ? 16 : 0),
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
                  BooleanDropdown(
                    label: "Is NPC?",
                    selected: _isNPC,
                    onChanged: (value) {
                      setState(() {
                        _isNPC = value;
                      });
                    },
                    isOptional: false,
                  ),
                  const SizedBox(height: 12),
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
