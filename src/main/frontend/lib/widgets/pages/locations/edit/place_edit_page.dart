import 'package:flutter/material.dart';
import 'package:frontend/models/location/place.dart';

class PlaceEditPage extends StatefulWidget {
  final String uuid;
  final Place place;
  const PlaceEditPage({super.key, required this.uuid, required this.place});

  @override
  State<PlaceEditPage> createState() => _PlaceEditPageState();
}

class _PlaceEditPageState extends State<PlaceEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
