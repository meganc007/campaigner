import 'package:flutter/material.dart';
import 'package:frontend/models/campaign.dart';
import 'package:frontend/models/items_overview.dart';
import 'package:frontend/models/locations_overview.dart';

class CampaignOverviewPage extends StatelessWidget {
  final Campaign campaign;
  final Future<LocationsOverview> futureLocations;
  final Future<ItemsOverview> futureItems;

  const CampaignOverviewPage({
    super.key,
    required this.campaign,
    required this.futureLocations,
    required this.futureItems,
  });

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: futureLocations,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator());
        } else if (snapshot.hasError) {
          return Center(child: Text('Error: ${snapshot.error}'));
        } else if (!snapshot.hasData) {
          return const Center(child: Text('No data available.'));
        }

        final locationOverview = snapshot.data!;
        return ListView(
          padding: const EdgeInsets.all(16),
          children: [
            Text(
              "Locations Overview",
              style: Theme.of(context).textTheme.headlineSmall,
            ),
            Text('Continents: ${locationOverview.continents.length}'),
            Text('Countries: ${locationOverview.countries.length}'),
            Text('Cities: ${locationOverview.cities.length}'),
            Text('Regions: ${locationOverview.regions.length}'),
            Text('Landmarks: ${locationOverview.landmarks.length}'),
            Text('Places: ${locationOverview.places.length}'),
            Text('Place Types: ${locationOverview.placeTypes.length}'),
            Text('Terrains: ${locationOverview.terrains.length}'),
            Text('Climates: ${locationOverview.climates.length}'),
          ],
        );
      },
    );
  }
}
