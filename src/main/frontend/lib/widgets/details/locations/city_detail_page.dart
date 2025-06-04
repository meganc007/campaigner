import 'package:flutter/material.dart';

class CityDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> wealthMap;
  final Map<int, String> countryMap;
  final Map<int, String> settlementTypeMap;
  final Map<int, String> governmentMap;
  final Map<int, String> regionMap;
  const CityDetailPage({
    super.key,
    required this.uuid,
    required this.wealthMap,
    required this.countryMap,
    required this.settlementTypeMap,
    required this.governmentMap,
    required this.regionMap,
  });

  @override
  State<CityDetailPage> createState() => _CityDetailPageState();
}

class _CityDetailPageState extends State<CityDetailPage> {
  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
