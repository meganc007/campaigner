import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/sun.dart';

class SunEditPage extends StatefulWidget {
  final String uuid;
  final Sun sun;
  const SunEditPage({super.key, required this.uuid, required this.sun});

  @override
  State<SunEditPage> createState() => _SunEditPageState();
}

class _SunEditPageState extends State<SunEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
