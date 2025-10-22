import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/week.dart';

class WeekEditPage extends StatefulWidget {
  final String uuid;
  final Week week;
  const WeekEditPage({super.key, required this.uuid, required this.week});

  @override
  State<WeekEditPage> createState() => _WeekEditPageState();
}

class _WeekEditPageState extends State<WeekEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
