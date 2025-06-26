import 'package:flutter/material.dart';
import 'package:frontend/models/items/weapon.dart';

class WeaponEditPage extends StatefulWidget {
  final String uuid;
  final Weapon weapon;
  const WeaponEditPage({super.key, required this.uuid, required this.weapon});

  @override
  State<WeaponEditPage> createState() => _WeaponEditPageState();
}

class _WeaponEditPageState extends State<WeaponEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
