import 'package:flutter/material.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/reusable/styled_dropdown.dart';

class EntityDropdown<T> extends StatelessWidget {
  final String label;
  final T? selected;
  final List<T> options;
  final String Function(T) getLabel;
  final void Function(T?) onChanged;
  final String? Function(T?)? validator;
  final bool isOptional;

  const EntityDropdown({
    super.key,
    required this.label,
    required this.selected,
    required this.options,
    required this.getLabel,
    required this.onChanged,
    this.validator,
    this.isOptional = false,
  });

  @override
  Widget build(BuildContext context) {
    final items = [
      if (isOptional)
        DropdownMenuItem<T>(value: null, child: const Text('None')),
      ...options.map((item) {
        return DropdownMenuItem<T>(value: item, child: Text(getLabel(item)));
      }),
    ];
    return StyledDropdown<T>(
      label: label,
      value: selected,
      items: items,
      onChanged: onChanged,
      validator: isOptional
          ? null
          : validator ??
                (value) => requiredDropdown(value, label.toLowerCase()),
    );
  }
}
