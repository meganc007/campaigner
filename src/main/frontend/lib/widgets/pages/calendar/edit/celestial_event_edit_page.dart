import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/celestial_event.dart';

class CelestialEventEditPage extends StatefulWidget {
  final String uuid;
  final CelestialEvent celestialEvent;
  const CelestialEventEditPage({
    super.key,
    required this.uuid,
    required this.celestialEvent,
  });

  @override
  State<CelestialEventEditPage> createState() => _CelestialEventEditPageState();
}

class _CelestialEventEditPageState extends State<CelestialEventEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
