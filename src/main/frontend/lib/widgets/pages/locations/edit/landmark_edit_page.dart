import 'package:flutter/material.dart';
import 'package:frontend/models/location/landmark.dart';

class LandmarkEditPage extends StatefulWidget {
  final String uuid;
  final Landmark landmark;
  const LandmarkEditPage({
    super.key,
    required this.uuid,
    required this.landmark,
  });

  @override
  State<LandmarkEditPage> createState() => _LandmarkEditPageState();
}

class _LandmarkEditPageState extends State<LandmarkEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
