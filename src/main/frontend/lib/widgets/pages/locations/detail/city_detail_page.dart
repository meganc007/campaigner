import 'package:flutter/material.dart';
import 'package:frontend/models/location/city.dart';
import 'package:frontend/services/locations/city_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/pages/locations/add/add_city_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';
import 'package:frontend/widgets/pages/locations/edit/city_edit_page.dart';

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
  late Future<List<City>> _futureCities;

  @override
  void initState() {
    super.initState();
    _futureCities = fetchCities(widget.uuid);
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureCities = fetchCities(widget.uuid);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Cities".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureCities,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              return Center(child: Text('Error: ${snapshot.error}'));
            } else if (!snapshot.hasData) {
              return const Center(child: Text('No data available.'));
            }

            final cities = snapshot.data!;

            final cityWidgets = cities
                .map(
                  (city) => DetailSection(
                    title: city.name,
                    fields: {
                      "Description": city.description,
                      "Wealth": widget.wealthMap[city.fkWealth] ?? 'Unknown',
                      "Country": widget.countryMap[city.fkCountry] ?? 'Unknown',
                      "Settlment type":
                          widget.settlementTypeMap[city.fkSettlement] ??
                          'Unknown',
                      "Government":
                          widget.governmentMap[city.fkGovernment] ?? 'Unknown',
                      "Region": widget.regionMap[city.fkRegion] ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              CityEditPage(uuid: widget.uuid, city: city),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: city.name,
                        type: "city",
                        onDelete: () => deleteCity(city.id),
                        onSuccess: _refreshData,
                      );
                    },
                  ),
                )
                .toList();

            return SafeArea(
              child: Center(
                child: ListView(
                  padding: const EdgeInsets.all(12),
                  children: cityWidgets
                      .map(
                        (widget) => Padding(
                          padding: const EdgeInsets.only(bottom: 12),
                          child: widget,
                        ),
                      )
                      .toList(),
                ),
              ),
            );
          },
        ),
      ),
      bottomNavigationBar: CreateNewButton(
        label: "Create City",
        destinationBuilder: (context) => AddCityPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
