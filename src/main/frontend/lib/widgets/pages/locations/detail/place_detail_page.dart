import 'package:flutter/material.dart';
import 'package:frontend/models/location/place.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/locations/place_service.dart';
import 'package:frontend/widgets/pages/locations/add/add_place_page.dart';
import 'package:frontend/widgets/pages/locations/edit/place_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class PlaceDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> placeTypeMap;
  final Map<int, String> terrainMap;
  final Map<int, String> countryMap;
  final Map<int, String> cityMap;
  final Map<int, String> regionMap;
  const PlaceDetailPage({
    super.key,
    required this.uuid,
    required this.placeTypeMap,
    required this.terrainMap,
    required this.countryMap,
    required this.cityMap,
    required this.regionMap,
  });

  @override
  State<PlaceDetailPage> createState() => _PlaceDetailPageState();
}

class _PlaceDetailPageState extends State<PlaceDetailPage> {
  late Future<List<Place>> _futurePlaces;

  @override
  void initState() {
    super.initState();
    _futurePlaces = fetchPlaces(widget.uuid);
  }

  Future<void> _refreshData() async {
    setState(() {
      _futurePlaces = fetchPlaces(widget.uuid);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Places".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futurePlaces,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              return Center(child: Text('Error: ${snapshot.error}'));
            } else if (!snapshot.hasData) {
              return const Center(child: Text('No data available.'));
            }

            final places = snapshot.data!;

            final placeWidgets = places
                .map(
                  (place) => DetailSection(
                    title: place.name,
                    fields: {
                      "Description": place.description,
                      "Place Type":
                          widget.placeTypeMap[place.fkPlaceType] ?? 'Unknown',
                      "Terrain":
                          widget.terrainMap[place.fkTerrain] ?? 'Unknown',
                      "Country":
                          widget.countryMap[place.fkCountry] ?? 'Unknown',
                      "Region": widget.regionMap[place.fkRegion] ?? 'Unknown',
                      "City": widget.cityMap[place.fkCity] ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              PlaceEditPage(uuid: widget.uuid, place: place),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: place.name,
                        type: "place",
                        onDelete: () => deletePlace(place.id),
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
                  children: placeWidgets
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
        label: "Create Place",
        destinationBuilder: (context) => AddPlacePage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
