import 'package:flutter/material.dart';
import 'package:frontend/models/campaign.dart';
import 'package:frontend/models/locations_overview.dart';
import 'package:frontend/services/locations_overview_service.dart';

class CampaignDetailPage extends StatefulWidget {
  final Campaign campaign;
  const CampaignDetailPage({super.key, required this.campaign});

  @override
  State<CampaignDetailPage> createState() => _CampaignDetailPageState();
}

class _CampaignDetailPageState extends State<CampaignDetailPage> {
  late Future<LocationsOverview> _futureOverview;

  @override
  void initState() {
    super.initState();
    _futureOverview = fetchLocationsOverview(widget.campaign.uuid);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(widget.campaign.name)),
      body: FutureBuilder(
        future: _futureOverview,
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
                'Continents: ${locationOverview.continents.length}',
                style: Theme.of(context).textTheme.titleMedium,
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
      ),
    );
  }
}
