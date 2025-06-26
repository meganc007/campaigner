import 'package:flutter/material.dart';
import 'package:frontend/models/items/weapon_type.dart';

class WeaponTypeEditPage extends StatefulWidget {
  final WeaponType weaponType;
  const WeaponTypeEditPage({super.key, required this.weaponType});

  @override
  State<WeaponTypeEditPage> createState() => _WeaponTypeEditPageState();
}

class _WeaponTypeEditPageState extends State<WeaponTypeEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
