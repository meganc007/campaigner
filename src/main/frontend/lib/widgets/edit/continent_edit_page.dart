import 'package:flutter/material.dart';
import 'package:frontend/models/location/continent.dart';
import 'package:frontend/services/continents_service.dart';
import 'package:frontend/widgets/styled_text_field.dart';

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

    if (mounted && success) {
      ScaffoldMessenger.of(localContext).showSnackBar(
        const SnackBar(content: Text("Continent edited successfully!")),
      );
      Navigator.pop(localContext, true);
    } else {
      ScaffoldMessenger.of(localContext).showSnackBar(
        const SnackBar(content: Text("Failed to edit continent.")),
      );
    }
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
                      : const Text("Update"),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
