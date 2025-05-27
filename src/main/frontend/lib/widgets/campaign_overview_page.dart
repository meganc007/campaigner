import 'package:flutter/material.dart';
import 'package:frontend/models/campaign.dart';
import 'package:frontend/models/locations_overview.dart';

class CampaignOverviewPage extends StatelessWidget {
  final Campaign campaign;
  final Future<LocationsOverview> futureLocations;
  const CampaignOverviewPage({
    super.key,
    required this.campaign,
    required this.futureLocations,
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
            SizedBox(
              height: 150,
              child: SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                child: Row(
                  children: [
                    Card(
                      elevation: 2,
                      shape: const RoundedRectangleBorder(
                        borderRadius: BorderRadius.zero,
                      ),
                      color: Colors.amber,
                      child: SizedBox(
                        height: 120,
                        width: 120,
                        child: Padding(
                          padding: const EdgeInsets.all(12),
                          child: Column(
                            mainAxisSize: MainAxisSize.min,
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            children: [
                              Text(
                                "Continents",
                                style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                              const Divider(
                                color: Colors.black,
                                thickness: 1,
                                indent: 0,
                                endIndent: 0,
                              ),
                              Text(
                                "count: ${locationOverview.continents.length}",
                              ),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
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
