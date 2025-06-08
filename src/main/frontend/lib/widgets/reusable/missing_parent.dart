import 'package:flutter/material.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';

class MissingParent extends StatelessWidget {
  final String parents;
  final bool show;
  final Future<void> Function(BuildContext context) onCreateTap;
  const MissingParent({
    super.key,
    required this.parents,
    required this.show,
    required this.onCreateTap,
  });

  @override
  Widget build(BuildContext context) {
    if (!show) return SizedBox.shrink();

    final message = "There are no $parents.";

    return DropdownDescription(
      message,
      color: Colors.red,
      linkText: "Create one?",
      onLinkTap: () async {
        await onCreateTap(context);
      },
    );
  }
}
