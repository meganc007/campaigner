import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/month.dart';
import 'package:frontend/services/calendar/month_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class MonthEditPage extends StatefulWidget {
  final String uuid;
  final Month month;
  const MonthEditPage({super.key, required this.uuid, required this.month});

  @override
  State<MonthEditPage> createState() => _MonthEditPageState();
}

class _MonthEditPageState extends State<MonthEditPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  String? _selectedSeason;

  @override
  void initState() {
    super.initState();
    _nameController.text = widget.month.name;
    _descriptionController.text = widget.month.description;
    _selectedSeason = widget.month.season;
  }

  @override
  void dispose() {
    _nameController.dispose();
    _descriptionController.dispose();
    super.dispose();
  }

  Future<void> _onSeasonChanged(String? newSeason) async {
    setState(() {
      _selectedSeason = newSeason;
    });
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await editMonth(
      widget.month.id,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      widget.uuid,
      _selectedSeason!,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Month",
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Edit ${widget.month.name}".toUpperCase())),
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
                EntityDropdown<String>(
                  label: "Season",
                  selected: _selectedSeason,
                  options: ["Spring", "Summer", "Autumn", "Winter"],
                  getLabel: (s) => s,
                  onChanged: _onSeasonChanged,
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
