import 'package:flutter/material.dart';

class StyledDropdown<T> extends StatelessWidget {
  final String label;
  final T? value;
  final List<DropdownMenuItem<T>> items;
  final void Function(T?) onChanged;
  final String? Function(T?)? validator;

  const StyledDropdown({
    super.key,
    required this.label,
    required this.value,
    required this.items,
    required this.onChanged,
    this.validator,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: const Color(0xFFFDF6E3),
        border: Border.all(width: 2, color: Colors.brown.shade700),
      ),
      child: DropdownButtonFormField<T>(
        value: value,
        items: items,
        onChanged: onChanged,
        validator: validator,
        isExpanded: true,
        decoration: InputDecoration(
          labelText: label,
          border: InputBorder.none,
          isDense: true,
        ),
        style: const TextStyle(
          fontFamily: "monospace",
          fontSize: 14,
          color: Colors.black,
        ),
        dropdownColor: const Color(0xFFFDF6E3),
        iconEnabledColor: Colors.brown.shade700,
      ),
    );
  }
}
