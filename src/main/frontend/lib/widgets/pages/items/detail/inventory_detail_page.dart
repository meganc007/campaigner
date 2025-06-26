import 'package:flutter/material.dart';

class InventoryDetailPage extends StatefulWidget {
  final String uuid;
  const InventoryDetailPage({super.key, required this.uuid});

  @override
  State<InventoryDetailPage> createState() => _InventoryDetailPageState();
}

class _InventoryDetailPageState extends State<InventoryDetailPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
