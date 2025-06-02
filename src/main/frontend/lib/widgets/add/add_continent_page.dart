import 'package:flutter/material.dart';
import 'package:frontend/services/continents_service.dart';
import 'package:frontend/widgets/styled_text_field.dart';

class AddContinentPage extends StatefulWidget {
  final String uuid;
  const AddContinentPage({super.key, required this.uuid});

  @override
  State<AddContinentPage> createState() => _AddContinentPageState();
}

class _AddContinentPageState extends State<AddContinentPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _isSubmitting = false;

  @override
  void dispose() {
    _nameController.dispose();
    _descriptionController.dispose();
    super.dispose();
  }

  Future<void> _submitForm() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() => _isSubmitting = true);

    final localContext = context; // capture the current BuildContext

    final success = await createContinent(
      widget.uuid,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    if (mounted && success) {
      ScaffoldMessenger.of(localContext).showSnackBar(
        const SnackBar(content: Text("Continent created successfully!")),
      );
      Navigator.pop(localContext, true);
    } else {
      ScaffoldMessenger.of(localContext).showSnackBar(
        const SnackBar(content: Text("Failed to create continent.")),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Create Continent".toUpperCase())),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: Center(
          child: Form(
            key: _formKey,
            child: Column(
              children: [
                StyledTextField(
                  controller: _nameController,
                  label: "Name",
                  validator: (value) =>
                      value == null || value.isEmpty ? 'Name required' : null,
                ),
                const SizedBox(height: 12),
                StyledTextField(
                  controller: _descriptionController,
                  label: "Description",
                  maxLines: 3,
                ),
                const SizedBox(height: 24),
                ElevatedButton(
                  onPressed: _isSubmitting ? null : _submitForm,
                  child: _isSubmitting
                      ? const CircularProgressIndicator()
                      : const Text("Create"),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
