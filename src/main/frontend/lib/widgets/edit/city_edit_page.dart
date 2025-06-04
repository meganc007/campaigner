import 'package:flutter/material.dart';
import 'package:frontend/models/location/city.dart';

class CityEditPage extends StatefulWidget {
  final String uuid;
  final City city;
  const CityEditPage({super.key, required this.uuid, required this.city});

  @override
  State<CityEditPage> createState() => _CityEditPageState();
}

class _CityEditPageState extends State<CityEditPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
