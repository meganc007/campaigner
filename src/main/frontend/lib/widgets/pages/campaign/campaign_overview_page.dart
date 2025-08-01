import 'package:flutter/material.dart';
import 'package:frontend/models/campaign.dart';
import 'package:frontend/models/items_overview.dart';
import 'package:frontend/models/locations_overview.dart';
import 'package:frontend/util/helpers.dart';

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
        final validationResult = futureBuilderValidation(snapshot);
        if (validationResult != null) return validationResult;

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
