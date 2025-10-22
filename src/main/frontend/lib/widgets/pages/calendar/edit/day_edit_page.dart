import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/day.dart';

class DayEditPage extends StatefulWidget {
  final String uuid;
  final Day day;
  const DayEditPage({super.key, required this.uuid, required this.day});

  @override
  State<DayEditPage> createState() => _DayEditPageState();
}

class _DayEditPageState extends State<DayEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
