import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/models/items/item_type.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/item_service.dart';
import 'package:frontend/services/items/item_type_service.dart';
import 'package:frontend/widgets/reusable/dropdown_boolean.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddItemPage extends StatefulWidget {
  final String uuid;
  const AddItemPage({super.key, required this.uuid});

  @override
  State<AddItemPage> createState() => _AddItemPageState();
}

class _AddItemPageState extends State<AddItemPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _rarityController = TextEditingController();
  final TextEditingController _goldValueController = TextEditingController();
  final TextEditingController _silverValueController = TextEditingController();
  final TextEditingController _copperValueController = TextEditingController();
  final TextEditingController _weightController = TextEditingController();
  bool? _isMagical;
  bool? _isCursed;
  final TextEditingController _notesController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<ItemType> _itemTypes = [];
  ItemType? _selectedItemType;
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
    _notesController.dispose();
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([fetchItemTypes()]);
      setState(() {
        _itemTypes = results[0];
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

    final success = await createItem(
      widget.uuid,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      _rarityController.text.trim(),
      goldValue,
      silverValue,
      copperValue,
      weightValue,
      _selectedItemType!.id,
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
      entityName: "Item",
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
      appBar: AppBar(title: Text("Create Item".toUpperCase())),
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
                  EntityDropdown<ItemType>(
                    label: "Item Type",
                    selected: _selectedItemType,
                    options: _itemTypes,
                    getLabel: (it) => it.name,
                    onChanged: (value) =>
                        setState(() => _selectedItemType = value),
                  ),
                  if (_selectedItemType != null)
                    DropdownDescription(_selectedItemType!.description ?? ''),
                  SizedBox(
                    height: _selectedItemType?.description != null ? 16 : 0,
                  ),
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
