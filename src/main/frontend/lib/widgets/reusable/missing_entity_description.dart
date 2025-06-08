import 'package:flutter/material.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';

class MissingEntityDescription extends StatelessWidget {
  final String children;
  final String parent;
  final bool show;
  final Future<void> Function(BuildContext context) onCreateTap;

  const MissingEntityDescription({
    super.key,
    required this.children,
    required this.parent,
    required this.show,
    required this.onCreateTap,
  });

  @override
  Widget build(BuildContext context) {
    if (!show) return SizedBox.shrink();

    final message = "There are no $children associated with that $parent.";

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
