import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/moon.dart';

class MoonEditPage extends StatefulWidget {
  final String uuid;
  final Moon moon;
  const MoonEditPage({super.key, required this.uuid, required this.moon});

  @override
  State<MoonEditPage> createState() => _MoonEditPageState();
}

class _MoonEditPageState extends State<MoonEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
