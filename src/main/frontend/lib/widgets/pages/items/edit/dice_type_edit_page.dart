import 'package:flutter/material.dart';
import 'package:frontend/models/items/dice_type.dart';

class DiceTypeEditPage extends StatefulWidget {
  final DiceType diceType;
  const DiceTypeEditPage({super.key, required this.diceType});

  @override
  State<DiceTypeEditPage> createState() => _DiceTypeEditPageState();
}

class _DiceTypeEditPageState extends State<DiceTypeEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
