import 'package:flutter/material.dart';
import 'package:frontend/models/items/damage_type.dart';

class DamageTypeEditPage extends StatefulWidget {
  final DamageType damageType;
  const DamageTypeEditPage({super.key, required this.damageType});

  @override
  State<DamageTypeEditPage> createState() => _DamageTypeEditPageState();
}

class _DamageTypeEditPageState extends State<DamageTypeEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
