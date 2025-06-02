import 'package:flutter/material.dart';
import 'package:frontend/models/locations_overview.dart';
import 'package:frontend/models/section.dart';
import 'package:frontend/services/locations_overview_service.dart';
import 'package:frontend/widgets/details/city_detail_page.dart';
import 'package:frontend/widgets/details/climate_detail_page.dart';
import 'package:frontend/widgets/details/continent_detail_page.dart';
import 'package:frontend/widgets/details/country_detail_page.dart';
import 'package:frontend/widgets/details/landmark_detail_page.dart';
import 'package:frontend/widgets/details/place_detail_page.dart';
import 'package:frontend/widgets/details/place_type_detail_page.dart';
import 'package:frontend/widgets/details/region_detail_page.dart';
import 'package:frontend/widgets/details/terrain_detail_page.dart';
import 'package:frontend/widgets/overviews/overview_section.dart';

class LocationOverviewPage extends StatefulWidget {
  final String uuid;
  final Future<LocationsOverview> futureLocations;

  const LocationOverviewPage({
    super.key,
    required this.uuid,
    required this.futureLocations,
  });

  @override
  State<LocationOverviewPage> createState() => _LocationOverviewPageState();
}

class _LocationOverviewPageState extends State<LocationOverviewPage> {
  late Future<LocationsOverview> _future;

  @override
  void initState() {
    super.initState();
    _future = widget.futureLocations;
  }

  Future<void> _refreshData() async {
    setState(() {
      _future = fetchLocationsOverview(widget.uuid);
    });
  }

  List<String> mapNamesToList(List<dynamic> items) {
    return items.map((item) => (item as dynamic).name as String).toList();
  }

  @override
  Widget build(BuildContext context) {
    return RefreshIndicator(
      onRefresh: _refreshData,
      child: FutureBuilder<LocationsOverview>(
        future: _future,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (!snapshot.hasData) {
            return const Center(child: Text('No data available.'));
          }

          final locationOverview = snapshot.data!;

          final List<Widget> sectionWidgets = [
            OverviewSection(
              section: Section(
                "Continents".toUpperCase(),
                mapNamesToList(locationOverview.continents),
              ),
              onSeeMore: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => ContinentDetailPage(uuid: widget.uuid),
                  ),
                );
              },
            ),
            OverviewSection(
              section: Section(
                "Countries".toUpperCase(),
                mapNamesToList(locationOverview.countries),
              ),
              onSeeMore: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => CountryDetailPage(uuid: widget.uuid),
                  ),
                );
              },
            ),
            OverviewSection(
              section: Section(
                "Cities".toUpperCase(),
                mapNamesToList(locationOverview.cities),
              ),
              onSeeMore: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => CityDetailPage(uuid: widget.uuid),
                  ),
                );
              },
            ),
            OverviewSection(
              section: Section(
                "Regions".toUpperCase(),
                mapNamesToList(locationOverview.regions),
              ),
              onSeeMore: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => RegionDetailPage(uuid: widget.uuid),
                  ),
                );
              },
            ),
            OverviewSection(
              section: Section(
                "Landmarks".toUpperCase(),
                mapNamesToList(locationOverview.landmarks),
              ),
              onSeeMore: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => LandmarkDetailPage(uuid: widget.uuid),
                  ),
                );
              },
            ),
            OverviewSection(
              section: Section(
                "Places".toUpperCase(),
                mapNamesToList(locationOverview.places),
              ),
              onSeeMore: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => PlaceDetailPage(uuid: widget.uuid),
                  ),
                );
              },
            ),
            OverviewSection(
              section: Section(
                "Place Types".toUpperCase(),
                mapNamesToList(locationOverview.placeTypes),
              ),
              onSeeMore: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => PlaceTypeDetailPage(uuid: widget.uuid),
                  ),
                );
              },
            ),
            OverviewSection(
              section: Section(
                "Terrain".toUpperCase(),
                mapNamesToList(locationOverview.terrains),
              ),
              onSeeMore: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => TerrainDetailPage(uuid: widget.uuid),
                  ),
                );
              },
            ),
            OverviewSection(
              section: Section(
                "Climates".toUpperCase(),
                mapNamesToList(locationOverview.climates),
              ),
              onSeeMore: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => ClimateDetailPage(uuid: widget.uuid),
                  ),
                );
              },
            ),
          ];

          return SafeArea(
            child: SingleChildScrollView(
              padding: const EdgeInsets.all(12),
              child: Center(
                child: Wrap(
                  spacing: 12,
                  runSpacing: 12,
                  children: sectionWidgets,
                ),
              ),
            ),
          );
        },
      ),
    );
  }
}
