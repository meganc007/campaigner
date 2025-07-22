import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/models/items/damage_type.dart';
import 'package:frontend/models/items/dice_type.dart';
import 'package:frontend/models/items/weapon_type.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/damage_type_service.dart';
import 'package:frontend/services/items/dice_type_service.dart';
import 'package:frontend/services/items/weapon_service.dart';
import 'package:frontend/services/items/weapon_type_service.dart';
import 'package:frontend/widgets/reusable/dropdown_boolean.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddWeaponPage extends StatefulWidget {
  final String uuid;
  const AddWeaponPage({super.key, required this.uuid});

  @override
  State<AddWeaponPage> createState() => _AddWeaponPageState();
}

class _AddWeaponPageState extends State<AddWeaponPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _rarityController = TextEditingController();
  final TextEditingController _goldValueController = TextEditingController();
  final TextEditingController _silverValueController = TextEditingController();
  final TextEditingController _copperValueController = TextEditingController();
  final TextEditingController _weightController = TextEditingController();
  final TextEditingController _numberOfDiceController = TextEditingController();
  final TextEditingController _damageModifierController =
      TextEditingController();
  bool? _isMagical;
  bool? _isCursed;
  final TextEditingController _notesController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<WeaponType> _weaponTypes = [];
  List<DamageType> _damageTypes = [];
  List<DiceType> _diceTypes = [];

  WeaponType? _selectedWeaponType;
  DamageType? _selectedDamageType;
  DiceType? _selectedDiceType;
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
    _rarityController.dispose();
    _goldValueController.dispose();
    _silverValueController.dispose();
    _copperValueController.dispose();
    _weightController.dispose();
    _numberOfDiceController.dispose();
    _damageModifierController.dispose();
    _notesController.dispose();
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([
        fetchWeaponTypes(),
        fetchDamageTypes(),
        fetchDiceTypes(),
      ]);
      setState(() {
        _weaponTypes = results[0] as List<WeaponType>;
        _damageTypes = results[1] as List<DamageType>;
        _diceTypes = results[2] as List<DiceType>;

        _weaponTypes.sort((a, b) => a.name.compareTo(b.name));
        _damageTypes.sort((a, b) => a.name.compareTo(b.name));
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

    final int goldValue = int.tryParse(_goldValueController.text.trim()) ?? 0;
    final int silverValue =
        int.tryParse(_silverValueController.text.trim()) ?? 0;
    final int copperValue =
        int.tryParse(_copperValueController.text.trim()) ?? 0;
    final double weightValue =
        double.tryParse(_weightController.text.trim()) ?? 0.0;
    final int numberOfDiceValue =
        int.tryParse(_numberOfDiceController.text.trim()) ?? 0;
    final int damageModifierValue =
        int.tryParse(_damageModifierController.text.trim()) ?? 0;

    final success = await createWeapon(
      widget.uuid,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      _rarityController.text.trim(),
      goldValue,
      silverValue,
      copperValue,
      weightValue,
      _selectedWeaponType!.id,
      _selectedDamageType!.id,
      _selectedDiceType!.id,
      numberOfDiceValue,
      damageModifierValue,
      _isMagical,
      _isCursed,
      _notesController.text.trim(),
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnCreate(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Weapon",
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
      appBar: AppBar(title: Text("Create Weapon".toUpperCase())),
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
                  StyledTextField(
                    controller: _rarityController,
                    label: "Rarity",
                  ),
                  const SizedBox(height: 12),
                  StyledTextField(
                    controller: _goldValueController,
                    label: 'Gold Value',
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
                  StyledTextField(
                    controller: _silverValueController,
                    label: 'Silver Value',
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
                  StyledTextField(
                    controller: _copperValueController,
                    label: 'Copper Value',
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
                  StyledTextField(
                    controller: _weightController,
                    label: 'Weight (lbs)',
                    keyboardType: TextInputType.number,
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    validator: (value) {
                      if (value == null || value.isEmpty) return 'Required';
                      if (double.tryParse(value) == null) {
                        return 'Enter a valid number';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 12),
                  EntityDropdown<WeaponType>(
                    label: "Weapon Type",
                    selected: _selectedWeaponType,
                    options: _weaponTypes,
                    getLabel: (wt) => wt.name,
                    onChanged: (value) =>
                        setState(() => _selectedWeaponType = value),
                  ),
                  if (_selectedWeaponType != null)
                    DropdownDescription(_selectedWeaponType!.description ?? ''),
                  const SizedBox(height: 16),
                  EntityDropdown(
                    label: "Damage Type",
                    selected: _selectedDamageType,
                    options: _damageTypes,
                    getLabel: (dt) => dt.name,
                    onChanged: (value) =>
                        setState(() => _selectedDamageType = value),
                  ),
                  if (_selectedDamageType != null)
                    DropdownDescription(_selectedDamageType!.description ?? ''),
                  const SizedBox(height: 16),
                  EntityDropdown(
                    label: "Dice Type",
                    selected: _selectedDiceType,
                    options: _diceTypes,
                    getLabel: (dice) => dice.name,
                    onChanged: (value) =>
                        setState(() => _selectedDiceType = value),
                  ),
                  if (_selectedDiceType != null)
                    DropdownDescription(_selectedDiceType!.description ?? ''),
                  const SizedBox(height: 16),
                  StyledTextField(
                    controller: _numberOfDiceController,
                    label: 'Number of Dice',
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
                  StyledTextField(
                    controller: _damageModifierController,
                    label: 'Damage Modifier',
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
                  BooleanDropdown(
                    label: 'Is Magical?',
                    selected: _isMagical,
                    onChanged: (value) {
                      setState(() {
                        _isMagical = value;
                      });
                    },
                    isOptional: false,
                  ),
                  const SizedBox(height: 12),
                  BooleanDropdown(
                    label: 'Is Cursed?',
                    selected: _isCursed,
                    onChanged: (value) {
                      setState(() {
                        _isCursed = value;
                      });
                    },
                    isOptional: false,
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
