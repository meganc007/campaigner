import 'package:flutter/material.dart';
import 'package:frontend/models/items/item_type.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/item_type_service.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class ItemTypeEditPage extends StatefulWidget {
  final ItemType itemType;
  const ItemTypeEditPage({super.key, required this.itemType});

  @override
  State<ItemTypeEditPage> createState() => _ItemTypeEditPageState();
}

class _ItemTypeEditPageState extends State<ItemTypeEditPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  @override
  void initState() {
    super.initState();
    _nameController.text = widget.itemType.name;
    widget.itemType.description != null
        ? _descriptionController.text = widget.itemType.description!
        : _descriptionController.text = '';
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

    final success = await editItemType(
      widget.itemType.id,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Item Type",
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Edit ${widget.itemType.name}".toUpperCase())),
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
                  label: "Edit",
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
