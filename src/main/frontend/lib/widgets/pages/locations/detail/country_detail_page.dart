import 'package:flutter/material.dart';
import 'package:frontend/models/location/country.dart';
import 'package:frontend/services/locations/country_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/pages/locations/add/add_country_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';
import 'package:frontend/widgets/pages/locations/edit/country_edit_page.dart';

class CountryDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> continentMap;
  final Map<int, String> governmentMap;
  const CountryDetailPage({
    super.key,
    required this.uuid,
    required this.continentMap,
    required this.governmentMap,
  });

  @override
  State<CountryDetailPage> createState() => _CountryDetailPageState();
}

class _CountryDetailPageState extends State<CountryDetailPage> {
  late Future<List<Country>> _futureCountries;

  @override
  void initState() {
    super.initState();
    _futureCountries = fetchCountries(widget.uuid);
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureCountries = fetchCountries(widget.uuid);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Countries".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureCountries,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              return Center(child: Text('Error: ${snapshot.error}'));
            } else if (!snapshot.hasData) {
              return const Center(child: Text('No data available.'));
            }

            final countries = snapshot.data!;

            final countryWidgets = countries
                .map(
                  (country) => DetailSection(
                    title: country.name,
                    fields: {
                      "Description": country.description,
                      "Continent":
                          widget.continentMap[country.fkContinent] ?? 'Unknown',
                      "Government":
                          widget.governmentMap[country.fkGovernment] ??
                          'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => CountryEditPage(
                            uuid: widget.uuid,
                            country: country,
                          ),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: country.name,
                        type: "country",
                        onDelete: () => deleteCountry(country.id),
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
                  children: countryWidgets
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
        label: "Create Country",
        destinationBuilder: (context) => AddCountryPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
