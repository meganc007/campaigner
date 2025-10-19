import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/ability_score_service.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddAbilityScorePage extends StatefulWidget {
  final String uuid;
  const AddAbilityScorePage({super.key, required this.uuid});

  @override
  State<AddAbilityScorePage> createState() => _AddAbilityScorePageState();
}

class _AddAbilityScorePageState extends State<AddAbilityScorePage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _strengthController = TextEditingController();
  final TextEditingController _dexterityController = TextEditingController();
  final TextEditingController _constitutionController = TextEditingController();
  final TextEditingController _intelligenceController = TextEditingController();
  final TextEditingController _wisdomController = TextEditingController();
  final TextEditingController _charismaController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  @override
  void dispose() {
    _strengthController.dispose();
    _dexterityController.dispose();
    _constitutionController.dispose();
    _intelligenceController.dispose();
    _wisdomController.dispose();
    _charismaController.dispose();
    super.dispose();
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final int strength = int.tryParse(_strengthController.text.trim()) ?? 1;
    final int dexterity = int.tryParse(_dexterityController.text.trim()) ?? 1;
    final int constitution =
        int.tryParse(_constitutionController.text.trim()) ?? 1;
    final int intelligence =
        int.tryParse(_intelligenceController.text.trim()) ?? 1;
    final int wisdom = int.tryParse(_wisdomController.text.trim()) ?? 1;
    final int charisma = int.tryParse(_charismaController.text.trim()) ?? 1;

    final success = await createAbilityScore(
      widget.uuid,
      strength,
      dexterity,
      constitution,
      intelligence,
      wisdom,
      charisma,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnCreate(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Ability Score",
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Create Ability Score".toUpperCase())),
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
                    controller: _strengthController,
                    label: 'Strength',
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
                    controller: _dexterityController,
                    label: 'Dexterity',
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
                    controller: _constitutionController,
                    label: 'Constitution',
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
                    controller: _intelligenceController,
                    label: 'Intelligence',
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
                    controller: _wisdomController,
                    label: 'Wisdom',
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
                    controller: _charismaController,
                    label: 'Charisma',
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
      ),
    );
  }
}
