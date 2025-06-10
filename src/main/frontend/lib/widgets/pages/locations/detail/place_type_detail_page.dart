import 'package:flutter/material.dart';
import 'package:frontend/models/location/place_type.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/locations/place_type_service.dart';
import 'package:frontend/widgets/pages/locations/add/add_place_type_page.dart';
import 'package:frontend/widgets/pages/locations/edit/place_type_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class PlaceTypeDetailPage extends StatefulWidget {
  const PlaceTypeDetailPage({super.key});

  @override
  State<PlaceTypeDetailPage> createState() => _PlaceTypeDetailPageState();
}

class _PlaceTypeDetailPageState extends State<PlaceTypeDetailPage> {
  late Future<List<PlaceType>> _futurePlaceTypes;

  @override
  void initState() {
    super.initState();
    _futurePlaceTypes = fetchPlaceTypes();
  }

  Future<void> _refreshData() async {
    setState(() {
      _futurePlaceTypes = fetchPlaceTypes();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Place Types".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futurePlaceTypes,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              return Center(child: Text('Error: ${snapshot.error}'));
            } else if (!snapshot.hasData) {
              return const Center(child: Text('No data available.'));
            }

            final placeTypes = snapshot.data!;

            final placeTypeWidgets = placeTypes
                .map(
                  (pt) => DetailSection(
                    title: pt.name,
                    fields: {"Description": pt.description},
                    onEdit: () async {
                      final result = await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => PlaceTypeEditPage(placeType: pt),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: pt.name,
                        type: "place type",
                        onDelete: () => deletePlaceType(pt.id),
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
                  children: placeTypeWidgets
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
        label: "Create Place Type",
        destinationBuilder: (context) => AddPlaceTypePage(),
        onReturn: _refreshData,
      ),
    );
  }
}
