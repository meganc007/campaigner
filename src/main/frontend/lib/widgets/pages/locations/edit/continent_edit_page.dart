import 'package:flutter/material.dart';
import 'package:frontend/models/location/continent.dart';
import 'package:frontend/services/locations/continent_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class ContinentEditPage extends StatefulWidget {
  final String uuid;
  final Continent continent;
  const ContinentEditPage({
    super.key,
    required this.uuid,
    required this.continent,
  });

  @override
  State<ContinentEditPage> createState() => _ContinentEditPageState();
}

class _ContinentEditPageState extends State<ContinentEditPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  @override
  void initState() {
    super.initState();
    _nameController.text = widget.continent.name;
    _descriptionController.text = widget.continent.description;
  }

  @override
  void dispose() {
    _nameController.dispose();
    _descriptionController.dispose();
    super.dispose();
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;
    final success = await editContinent(
      widget.uuid,
      widget.continent.id,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Continent",
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Edit ${widget.continent.name}".toUpperCase()),
      ),
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
    );
  }
}
