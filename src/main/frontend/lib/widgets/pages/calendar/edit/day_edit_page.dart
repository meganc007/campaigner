import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/day.dart';
import 'package:frontend/models/calendar/week.dart';
import 'package:frontend/services/calendar/day_service.dart';
import 'package:frontend/services/calendar/week_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class DayEditPage extends StatefulWidget {
  final String uuid;
  final Day day;
  const DayEditPage({super.key, required this.uuid, required this.day});

  @override
  State<DayEditPage> createState() => _DayEditPageState();
}

class _DayEditPageState extends State<DayEditPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Week> _weeks = [];
  Week? _selectedWeek;
  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
    _nameController.text = widget.day.name;
    _descriptionController.text = widget.day.description;
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([fetchWeeks(widget.uuid)]);
      setState(() {
        _weeks = results[0];
        _selectedWeek = _weeks.firstWhereOrNull(
          (w) => w.id == widget.day.fkWeek,
        );
        _loading = false;
      });
    } catch (e) {
      setState(() {
        _error = e.toString();
        _loading = false;
      });
    }
  }

  @override
  void dispose() {
    _nameController.dispose();
    _descriptionController.dispose();
    super.dispose();
  }

  Future<void> _onWeekChanged(Week? newWeek) async {
    setState(() {
      _selectedWeek = newWeek;
    });
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await editDay(
      widget.day.id,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      widget.uuid,
      _selectedWeek!.id,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Day",
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
      appBar: AppBar(
        title: Text("Create Edit ${widget.day.name}".toUpperCase()),
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
                const SizedBox(height: 12),
                EntityDropdown<Week>(
                  label: "Week",
                  selected: _selectedWeek,
                  options: _weeks,
                  getLabel: (w) => w.weekNumber.toString(),
                  onChanged: _onWeekChanged,
                ),
                if (_selectedWeek != null)
                  DropdownDescription(_selectedWeek!.description),
                SizedBox(height: _selectedWeek?.description != null ? 16 : 0),
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
