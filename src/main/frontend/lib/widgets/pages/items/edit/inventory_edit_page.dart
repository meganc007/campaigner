import 'package:flutter/material.dart';
import 'package:frontend/models/items/inventory.dart';

class InventoryEditPage extends StatefulWidget {
  final String uuid;
  final Inventory inventory;
  const InventoryEditPage({
    super.key,
    required this.uuid,
    required this.inventory,
  });

  @override
  State<InventoryEditPage> createState() => _InventoryEditPageState();
}

class _InventoryEditPageState extends State<InventoryEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
