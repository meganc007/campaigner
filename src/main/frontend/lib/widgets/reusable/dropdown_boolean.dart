import 'package:flutter/material.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';

class BooleanDropdown extends StatelessWidget {
  final String label;
  final bool? selected;
  final void Function(bool?) onChanged;
  final String? Function(bool?)? validator;
  final bool isOptional;

  const BooleanDropdown({
    super.key,
    required this.label,
    required this.selected,
    required this.onChanged,
    this.validator,
    this.isOptional = false,
  });

  @override
  Widget build(BuildContext context) {
    return EntityDropdown<bool>(
      label: label,
      selected: selected,
      options: const [true, false],
      getLabel: (value) => value ? 'Yes' : 'No',
      onChanged: onChanged,
      validator: validator,
      isOptional: isOptional,
    );
  }
}
