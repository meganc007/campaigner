import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/models/people/ability_score.dart';
import 'package:frontend/models/people/race.dart';
import 'package:frontend/models/common/wealth.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/ability_score_service.dart';
import 'package:frontend/services/people/person_service.dart';
import 'package:frontend/services/people/race_service.dart';
import 'package:frontend/services/wealth_service.dart';
import 'package:frontend/widgets/reusable/dropdown_boolean.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddPersonPage extends StatefulWidget {
  final String uuid;
  const AddPersonPage({super.key, required this.uuid});

  @override
  State<AddPersonPage> createState() => _AddPersonPageState();
}

class _AddPersonPageState extends State<AddPersonPage> {
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

    final success = await createPerson(
      _firstNameController.text.trim(),
      _lastNameController.text.trim(),
      int.tryParse(_ageController.text.trim()),
      _titleController.text.trim(),
      _selectedRace!.id,
      _selectedWealth!.id,
      _selectedAbilityScore!.id,
      _isNPC!,
      _isEnemy!,
      _personalityController.text.trim(),
      _descriptionController.text.trim(),
      _notesController.text.trim(),
      widget.uuid,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnCreate(
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
      appBar: AppBar(title: Text("Create Person".toUpperCase())),
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
                    onChanged: (value) => setState(() => _selectedRace = value),
                  ),
                  if (_selectedRace != null)
                    DropdownDescription(_selectedRace!.description),
                  SizedBox(height: _selectedRace?.description != null ? 16 : 0),
                  EntityDropdown<Wealth>(
                    label: "Wealth",
                    selected: _selectedWealth,
                    options: _wealth,
                    getLabel: (w) => w.name,
                    onChanged: (value) =>
                        setState(() => _selectedWealth = value),
                  ),
                  const SizedBox(height: 12),
                  EntityDropdown<AbilityScore>(
                    label: "Ability Score",
                    selected: _selectedAbilityScore,
                    options: _abilityScores,
                    getLabel: (abs) => abs.toShortString(),
                    onChanged: (value) =>
                        setState(() => _selectedAbilityScore = value),
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
