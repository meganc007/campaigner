import 'package:flutter/material.dart';
import 'package:frontend/models/common/campaign.dart';
import 'package:frontend/models/overviews/calendar_overview.dart';
import 'package:frontend/models/overviews/items_overview.dart';
import 'package:frontend/models/overviews/locations_overview.dart';
import 'package:frontend/models/overviews/people_overview.dart';
import 'package:frontend/services/data%20providers/calendar_data_provider.dart';
import 'package:frontend/services/data%20providers/item_data_provider.dart';
import 'package:frontend/services/data%20providers/location_data_provider.dart';
import 'package:frontend/services/data%20providers/people_data_provider.dart';
import 'package:frontend/services/overviews/calendar_overview_service.dart';
import 'package:frontend/services/overviews/items_overview_service.dart';
import 'package:frontend/services/overviews/locations_overview_service.dart';
import 'package:frontend/services/overviews/people_overview_service.dart';
import 'package:frontend/widgets/pages/overviews/calendar_overview_page.dart';
import 'package:frontend/widgets/pages/overviews/campaign_overview_page.dart';
import 'package:frontend/widgets/pages/overviews/common_overview_page.dart';
import 'package:frontend/widgets/pages/overviews/items_overview_page.dart';
import 'package:frontend/widgets/pages/overviews/location_overview_page.dart';
import 'package:frontend/widgets/main_nav.dart';
import 'package:frontend/widgets/pages/people/people_overview_page.dart';
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
  late Future<ItemsOverview> _futureItems;
  late Future<PeopleOverview> _futurePeople;
  late Future<CalendarOverview> _futureCalendar;
  int _currentIndex = 0;

  @override
  void initState() {
    super.initState();
    _futureLocations = fetchLocationsOverview(widget.campaign.uuid);
    _futureItems = fetchItemsOverview(widget.campaign.uuid);
    _futurePeople = fetchPeopleOverview(widget.campaign.uuid);
    _futureCalendar = fetchCalendarOverview(widget.campaign.uuid);
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
        futureItems: _futureItems,
        futurePeople: _futurePeople,
      ),
      ChangeNotifierProvider<LocationDataProvider>(
        create: (_) => LocationDataProvider(widget.campaign.uuid),
        child: LocationOverviewPage(
          uuid: widget.campaign.uuid,
          futureLocations: _futureLocations,
        ),
      ),
      ChangeNotifierProvider(
        create: (_) => PeopleDataProvider(widget.campaign.uuid),
        child: PeopleOverviewPage(
          uuid: widget.campaign.uuid,
          futurePeople: _futurePeople,
        ),
      ),
      ChangeNotifierProvider(
        create: (_) => ItemDataProvider(widget.campaign.uuid),
        child: ItemsOverviewPage(
          uuid: widget.campaign.uuid,
          futureItems: _futureItems,
        ),
      ),
      QuestsOverviewPage(),
      ChangeNotifierProvider(
        create: (_) => CalendarDataProvider(widget.campaign.uuid),
        child: CalendarOverviewPage(
          uuid: widget.campaign.uuid,
          futureCalendar: _futureCalendar,
        ),
      ),

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
