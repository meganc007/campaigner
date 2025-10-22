import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/month.dart';

class MonthEditPage extends StatefulWidget {
  final String uuid;
  final Month month;
  const MonthEditPage({super.key, required this.uuid, required this.month});

  @override
  State<MonthEditPage> createState() => _MonthEditPageState();
}

class _MonthEditPageState extends State<MonthEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
