import 'package:flutter/material.dart';
import 'package:frontend/models/items/item.dart';

class ItemEditPage extends StatefulWidget {
  final String uuid;
  final Item item;
  const ItemEditPage({super.key, required this.uuid, required this.item});

  @override
  State<ItemEditPage> createState() => _ItemEditPageState();
}

class _ItemEditPageState extends State<ItemEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
