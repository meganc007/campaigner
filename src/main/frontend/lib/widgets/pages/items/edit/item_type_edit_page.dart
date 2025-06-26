import 'package:flutter/material.dart';
import 'package:frontend/models/items/item_type.dart';

class ItemTypeEditPage extends StatefulWidget {
  final ItemType itemType;
  const ItemTypeEditPage({super.key, required this.itemType});

  @override
  State<ItemTypeEditPage> createState() => _ItemTypeEditPageState();
}

class _ItemTypeEditPageState extends State<ItemTypeEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
