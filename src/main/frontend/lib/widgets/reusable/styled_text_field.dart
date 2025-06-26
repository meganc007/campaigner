import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class StyledTextField extends StatelessWidget {
  final TextEditingController controller;
  final String label;
  final int maxLines;
  final String? Function(String?)? validator;
  final List<TextInputFormatter>? inputFormatters;
  final TextInputType? keyboardType;
  const StyledTextField({
    super.key,
    required this.controller,
    required this.label,
    this.maxLines = 1,
    this.validator,
    this.inputFormatters,
    this.keyboardType,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: const Color(0xFFFDF6E3),
        border: Border.all(width: 2, color: Colors.brown.shade700),
      ),
      child: TextFormField(
        controller: controller,
        maxLines: maxLines,
        validator: validator,
        inputFormatters: inputFormatters,
        keyboardType: keyboardType,
        decoration: InputDecoration(
          labelText: label,
          border: InputBorder.none,
          isDense: true,
        ),
        style: const TextStyle(fontFamily: "monospace", fontSize: 14),
      ),
    );
  }
}
