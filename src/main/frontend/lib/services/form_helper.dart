import 'package:flutter/material.dart';

void handleSuccessOrFailureOnCreate({
  required BuildContext context,
  required bool success,
  required bool isMounted,
  required String entityName,
}) {
  if (!isMounted) return;

  final messenger = ScaffoldMessenger.of(context);

  if (success) {
    messenger.showSnackBar(
      SnackBar(content: Text("$entityName created successfully!")),
    );
    Navigator.pop(context, true);
  } else {
    messenger.showSnackBar(
      SnackBar(content: Text("Failed to create ${entityName.toLowerCase()}.")),
    );
  }
}

void handleSuccessOrFailureOnEdit({
  required BuildContext context,
  required bool success,
  required bool isMounted,
  required String entityName,
}) {
  if (!isMounted) return;

  final messenger = ScaffoldMessenger.of(context);

  if (success) {
    messenger.showSnackBar(
      SnackBar(content: Text("$entityName edited successfully!")),
    );
    Navigator.pop(context, true);
  } else {
    messenger.showSnackBar(
      SnackBar(content: Text("Failed to edit ${entityName.toLowerCase()}.")),
    );
  }
}

String? isNameValid(String? value) {
  return value == null || value.isEmpty ? 'Name required' : null;
}

String? requiredDropdown<T>(T? value, String entityName) {
  return value == null ? 'Please select a $entityName' : null;
}

Future<bool> showDeleteConfirmationDialog({
  required BuildContext context,
  required String name,
  required String type,
}) async {
  return await showDialog<bool>(
        context: context,
        builder: (ctx) => AlertDialog(
          title: Text('Delete $name?'),
          content: Text('Are you sure you want to delete this $type?'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(ctx, false),
              child: const Text('Cancel'),
            ),
            TextButton(
              onPressed: () => Navigator.pop(ctx, true),
              child: const Text('Delete'),
            ),
          ],
        ),
      ) ??
      false;
}
