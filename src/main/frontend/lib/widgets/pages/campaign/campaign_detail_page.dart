import 'package:flutter/material.dart';
import 'package:frontend/models/campaign.dart';
import 'package:frontend/models/locations_overview.dart';
import 'package:frontend/services/data%20providers/location_data_provider.dart';
import 'package:frontend/services/locations_overview_service.dart';
import 'package:frontend/widgets/pages/overviews/calendar_overview_page.dart';
import 'package:frontend/widgets/pages/campaign/campaign_overview_page.dart';
import 'package:frontend/widgets/pages/overviews/common_overview_page.dart';
import 'package:frontend/widgets/pages/overviews/items_overview_page.dart';
import 'package:frontend/widgets/pages/locations/location_overview_page.dart';
import 'package:frontend/widgets/main_nav.dart';
import 'package:frontend/widgets/pages/overviews/people_overview_page.dart';
import 'package:frontend/widgets/pages/overviews/quests_overview_page.dart';
import 'package:provider/provider.dart';

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
    final titles = [
      (widget.campaign.name),
      "Locations Overview",
      "People Overview",
      "Items Overview",
      "Quests Overview",
      "Calendar Overview",
      "Common Overview",
    ];
    final List<Widget> overviews = [
      CampaignOverviewPage(
        campaign: widget.campaign,
        futureLocations: _futureLocations,
      ),
      ChangeNotifierProvider<LocationDataProvider>(
        create: (_) => LocationDataProvider(widget.campaign.uuid),
        child: LocationOverviewPage(
          uuid: widget.campaign.uuid,
          futureLocations: _futureLocations,
        ),
      ),
      PeopleOverviewPage(),
      ItemsOverviewPage(),
      QuestsOverviewPage(),
      CalendarOverviewPage(),
      CommonOverviewPage(),
    ];

    return Scaffold(
      appBar: AppBar(title: Text(titles[_currentIndex].toUpperCase())),
      body: overviews[_currentIndex],
      drawer: MainNav(
        campaignName: widget.campaign.name,
        currentIndex: _currentIndex,
        onSelect: (index) {
          setState(() => _currentIndex = index);
        },
      ),
    );
  }
}
