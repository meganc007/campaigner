import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/dice_type_service.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddDiceTypePage extends StatefulWidget {
  const AddDiceTypePage({super.key});

  @override
  State<AddDiceTypePage> createState() => _AddDiceTypePageState();
}

class _AddDiceTypePageState extends State<AddDiceTypePage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _maxRollController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  @override
  void dispose() {
    _nameController.dispose();
    _descriptionController.dispose();
    _maxRollController.dispose();
    super.dispose();
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final int maxRoll = int.tryParse(_maxRollController.text.trim()) ?? 0;

    final success = await createDiceType(
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      maxRoll,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnCreate(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Dice Type",
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Create Dice Type".toUpperCase())),
      body: Padding(
        padding: const EdgeInsets.all(24),
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
                  controller: _maxRollController,
                  label: 'Max Roll',
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
    );
  }
}
