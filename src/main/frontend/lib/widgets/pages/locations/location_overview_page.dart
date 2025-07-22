import 'package:flutter/material.dart';
import 'package:frontend/models/locations_overview.dart';
import 'package:frontend/models/section.dart';
import 'package:frontend/services/data%20providers/location_data_provider.dart';
import 'package:frontend/services/locations_overview_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/locations/detail/city_detail_page.dart';
import 'package:frontend/widgets/pages/climate_detail_page.dart';
import 'package:frontend/widgets/pages/locations/detail/continent_detail_page.dart';
import 'package:frontend/widgets/pages/locations/detail/country_detail_page.dart';
import 'package:frontend/widgets/pages/locations/detail/landmark_detail_page.dart';
import 'package:frontend/widgets/pages/locations/detail/place_detail_page.dart';
import 'package:frontend/widgets/pages/locations/detail/place_type_detail_page.dart';
import 'package:frontend/widgets/pages/locations/detail/region_detail_page.dart';
import 'package:frontend/widgets/pages/locations/detail/terrain_detail_page.dart';
import 'package:frontend/widgets/pages/overviews/overview_section.dart';
import 'package:provider/provider.dart';

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

    final provider = context.read<LocationDataProvider>();
    await provider.load();
  }

  List<String> mapNamesToList(List<dynamic> items) {
    return items.map((item) => (item as dynamic).name as String).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<LocationDataProvider>(
      builder: (context, locationDataProvider, child) {
        if (locationDataProvider.isLoading) {
          return const Center(child: CircularProgressIndicator());
        }

        return RefreshIndicator(
          onRefresh: _refreshData,
          child: FutureBuilder<LocationsOverview>(
            future: _future,
            builder: (context, snapshot) {
              final validationResult = futureBuilderValidation(snapshot);
              if (validationResult != null) return validationResult;

              final locationOverview = snapshot.data!;

              final List<Widget> sectionWidgets = [
                OverviewSection(
                  section: Section(
                    "Continents".toUpperCase(),
                    mapNamesToList(locationOverview.continents),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => ContinentDetailPage(uuid: widget.uuid),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Countries".toUpperCase(),
                    mapNamesToList(locationOverview.countries),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => CountryDetailPage(
                          uuid: widget.uuid,
                          continentMap: locationDataProvider.continentMap,
                          governmentMap: locationDataProvider.governmentMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Regions".toUpperCase(),
                    mapNamesToList(locationOverview.regions),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => RegionDetailPage(
                          uuid: widget.uuid,
                          countryMap: locationDataProvider.countryMap,
                          climateMap: locationDataProvider.climateMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Landmarks".toUpperCase(),
                    mapNamesToList(locationOverview.landmarks),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => LandmarkDetailPage(
                          uuid: widget.uuid,
                          regionMap: locationDataProvider.regionMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Cities".toUpperCase(),
                    mapNamesToList(locationOverview.cities),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => CityDetailPage(
                          uuid: widget.uuid,
                          wealthMap: locationDataProvider.wealthMap,
                          countryMap: locationDataProvider.countryMap,
                          settlementTypeMap:
                              locationDataProvider.settlementTypeMap,
                          governmentMap: locationDataProvider.governmentMap,
                          regionMap: locationDataProvider.regionMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Places".toUpperCase(),
                    mapNamesToList(locationOverview.places),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => PlaceDetailPage(
                          uuid: widget.uuid,
                          placeTypeMap: locationDataProvider.placeTypeMap,
                          terrainMap: locationDataProvider.terrainMap,
                          countryMap: locationDataProvider.countryMap,
                          cityMap: locationDataProvider.cityMap,
                          regionMap: locationDataProvider.regionMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Place Types".toUpperCase(),
                    mapNamesToList(locationOverview.placeTypes),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => PlaceTypeDetailPage()),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Terrain".toUpperCase(),
                    mapNamesToList(locationOverview.terrains),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => TerrainDetailPage()),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Climates".toUpperCase(),
                    mapNamesToList(locationOverview.climates),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => ClimateDetailPage(uuid: widget.uuid),
                      ),
                    );
                    await _refreshData();
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
      },
    );
  }
}
