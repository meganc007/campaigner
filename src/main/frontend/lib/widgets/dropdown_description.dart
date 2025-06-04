import 'package:flutter/material.dart';

class DropdownDescription extends StatelessWidget {
  final String description;

  const DropdownDescription(this.description, {super.key});

  @override
  Widget build(BuildContext context) {
    return Text(
      description,
      style: TextStyle(
        fontFamily: "monospace",
        fontSize: 14,
        color: Colors.grey[700],
      ),
    );
  }
}
