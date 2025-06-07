import 'package:flutter/material.dart';
import 'package:frontend/models/location/continent.dart';
import 'package:frontend/services/continent_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/widgets/pages/locations/add/add_continent_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';
import 'package:frontend/widgets/pages/locations/edit/continent_edit_page.dart';

class ContinentDetailPage extends StatefulWidget {
  final String uuid;
  const ContinentDetailPage({super.key, required this.uuid});

  @override
  State<ContinentDetailPage> createState() => _ContinentDetailPageState();
}

class _ContinentDetailPageState extends State<ContinentDetailPage> {
  late Future<List<Continent>> _futureContinents;

  @override
  void initState() {
    super.initState();
    _futureContinents = fetchContinents(widget.uuid);
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureContinents = fetchContinents(widget.uuid);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Continents".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureContinents,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              return Center(child: Text('Error: ${snapshot.error}'));
            } else if (!snapshot.hasData) {
              return const Center(child: Text('No data available.'));
            }

            final continents = snapshot.data!;

            final continentWidgets = continents
                .map(
                  (continent) => DetailSection(
                    title: continent.name,
                    fields: {"Description": continent.description},
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => ContinentEditPage(
                            uuid: widget.uuid,
                            continent: continent,
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
                        name: continent.name,
                        type: "continent",
                        onDelete: () => deleteContinent(continent.id),
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
                  children: continentWidgets
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
        label: "Create Continent",
        destinationBuilder: (context) => AddContinentPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
