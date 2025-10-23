import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/models/calendar/month.dart';
import 'package:frontend/models/calendar/week.dart';
import 'package:frontend/services/calendar/month_service.dart';
import 'package:frontend/services/calendar/week_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class WeekEditPage extends StatefulWidget {
  final String uuid;
  final Week week;
  const WeekEditPage({super.key, required this.uuid, required this.week});

  @override
  State<WeekEditPage> createState() => _WeekEditPageState();
}

class _WeekEditPageState extends State<WeekEditPage> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  final TextEditingController _weekNumberController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Month> _months = [];
  Month? _selectedMonth;
  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
    _nameController.text = widget.week.name;
    _descriptionController.text = widget.week.description;
    _weekNumberController.text = widget.week.weekNumber.toString();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([fetchMonths(widget.uuid)]);
      setState(() {
        _months = results[0];
        _selectedMonth = _months.firstWhereOrNull(
          (m) => m.id == widget.week.fkMonth,
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

  Future<void> _onMonthChanged(Month? newMonth) async {
    setState(() {
      _selectedMonth = newMonth;
    });
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await editWeek(
      widget.week.id,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      widget.uuid,
      int.parse(_weekNumberController.text.trim()),
      _selectedMonth!.id,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Week",
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
      appBar: AppBar(title: Text("Edit ${widget.week.name}".toUpperCase())),
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
                  controller: _weekNumberController,
                  label: "Week Number",
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
                EntityDropdown<Month>(
                  label: "Month",
                  selected: _selectedMonth,
                  options: _months,
                  getLabel: (m) => m.name,
                  onChanged: _onMonthChanged,
                ),
                if (_selectedMonth != null)
                  DropdownDescription(_selectedMonth!.description),
                SizedBox(height: _selectedMonth?.description != null ? 16 : 0),
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
