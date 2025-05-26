import 'package:flutter/material.dart';
import 'package:frontend/models/campaign.dart';
import 'package:frontend/models/locations_overview.dart';
import 'package:frontend/services/locations_overview_service.dart';
import 'package:frontend/widgets/bottom_nav.dart';
import 'package:frontend/widgets/campaign_overview_page.dart';
import 'package:frontend/widgets/location_overview_page.dart';

class CampaignDetailPage extends StatefulWidget {
  final Campaign campaign;
  const CampaignDetailPage({super.key, required this.campaign});

  @override
  State<CampaignDetailPage> createState() => _CampaignDetailPageState();
}

class _CampaignDetailPageState extends State<CampaignDetailPage> {
  late Future<LocationsOverview> _futureLocations;
  int _currentIndex = 0;

  @override
  void initState() {
    super.initState();
    _futureLocations = fetchLocationsOverview(widget.campaign.uuid);
  }

  @override
  Widget build(BuildContext context) {
    final titles = ["${widget.campaign.name} - Overview", "Locations Overview"];
    final List<Widget> overviews = [
      CampaignOverviewPage(
        campaign: widget.campaign,
        futureLocations: _futureLocations,
      ),
      LocationOverviewPage(
        uuid: widget.campaign.uuid,
        futureLocations: _futureLocations,
      ),
    ];

    return Scaffold(
      appBar: AppBar(title: Text(titles[_currentIndex])),
      body: overviews[_currentIndex],
      bottomNavigationBar: BottomNav(
        currentIndex: _currentIndex,
        onTap: (index) => setState(() => _currentIndex = index),
      ),
    );
  }
}
