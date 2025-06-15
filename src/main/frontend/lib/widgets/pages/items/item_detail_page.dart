import 'package:flutter/material.dart';

class ItemDetailPage extends StatefulWidget {
  final String uuid;
  const ItemDetailPage({super.key, required this.uuid});

  @override
  State<ItemDetailPage> createState() => _ItemDetailPageState();
}

class _ItemDetailPageState extends State<ItemDetailPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
