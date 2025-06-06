import 'package:flutter/material.dart';

class CreateNewButton extends StatelessWidget {
  final String label;
  final Widget Function(BuildContext) destinationBuilder;
  final Future<void> Function()? onReturn;

  const CreateNewButton({
    super.key,
    required this.label,
    required this.destinationBuilder,
    this.onReturn,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: SizedBox(
        width: double.infinity,
        child: OutlinedButton(
          style: OutlinedButton.styleFrom(
            shape: const RoundedRectangleBorder(
              borderRadius: BorderRadius.zero,
            ),
            side: const BorderSide(
              width: 2,
              color: Color.fromRGBO(93, 64, 55, 1),
            ),
            padding: const EdgeInsets.symmetric(vertical: 16),
          ),
          onPressed: () async {
            final result = await Navigator.push<bool>(
              context,
              MaterialPageRoute(builder: destinationBuilder),
            );
            if (result == true && onReturn != null) {
              await onReturn!();
            }
          },
          child: Text(
            label.toUpperCase(),
            style: const TextStyle(color: Colors.black),
          ),
        ),
      ),
    );
  }
}
