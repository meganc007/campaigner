import 'package:flutter/material.dart';
import 'package:frontend/models/overviews/calendar_overview.dart';
import 'package:frontend/services/data%20providers/calendar_data_provider.dart';
import 'package:frontend/services/overviews/calendar_overview_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/calendar/detail/celestial_event_detail_page.dart';
import 'package:frontend/widgets/pages/calendar/detail/day_detail_page.dart';
import 'package:frontend/widgets/pages/calendar/detail/month_detail_page.dart';
import 'package:frontend/widgets/pages/calendar/detail/sun_detail_page.dart';
import 'package:frontend/widgets/pages/calendar/detail/week_detail_page.dart';
import 'package:frontend/widgets/pages/overviews/overview_section.dart';
import 'package:frontend/widgets/reusable/section.dart';
import 'package:provider/provider.dart';

class CalendarOverviewPage extends StatefulWidget {
  final String uuid;
  final Future<CalendarOverview> futureCalendar;

  const CalendarOverviewPage({
    super.key,
    required this.uuid,
    required this.futureCalendar,
  });

  @override
  State<CalendarOverviewPage> createState() => _CalendarOverviewPageState();
}

class _CalendarOverviewPageState extends State<CalendarOverviewPage> {
  late Future<CalendarOverview> _future;

  @override
  void initState() {
    super.initState();
    _future = widget.futureCalendar;
  }

  Future<void> _refreshData() async {
    setState(() {
      _future = fetchCalendarOverview(widget.uuid);
    });

    final provider = context.read<CalendarDataProvider>();
    await provider.load();
  }

  List<String> mapNamesToList(List<dynamic> items) {
    return items.map((item) => (item as dynamic).name as String).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<CalendarDataProvider>(
      builder: (context, calendarDataProvider, child) {
        if (calendarDataProvider.isLoading) {
          return const Center(child: CircularProgressIndicator());
        }

        return RefreshIndicator(
          onRefresh: _refreshData,
          child: FutureBuilder<CalendarOverview>(
            future: _future,
            builder: (context, snapshot) {
              final validationResult = futureBuilderValidation(snapshot);
              if (validationResult != null) return validationResult;

              final calendarOverview = snapshot.data!;

              final List<Widget> sectionWidgets = [
                OverviewSection(
                  section: Section(
                    "Suns".toUpperCase(),
                    mapNamesToList(calendarOverview.suns),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => SunDetailPage(uuid: widget.uuid),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Moons".toUpperCase(),
                    mapNamesToList(calendarOverview.moons),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => SunDetailPage(uuid: widget.uuid),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Months".toUpperCase(),
                    mapNamesToList(calendarOverview.months),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => MonthDetailPage(uuid: widget.uuid),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Weeks".toUpperCase(),
                    mapNamesToList(calendarOverview.weeks),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => WeekDetailPage(
                          uuid: widget.uuid,
                          monthMap: calendarDataProvider.monthMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Days".toUpperCase(),
                    mapNamesToList(calendarOverview.days),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => DayDetailPage(
                          uuid: widget.uuid,
                          weekMap: calendarDataProvider.weekMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Celestial Events".toUpperCase(),
                    mapNamesToList(calendarOverview.celestialEvents),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => CelestialEventDetailPage(
                          uuid: widget.uuid,
                          moonMap: calendarDataProvider.moonMap,
                          sunMap: calendarDataProvider.sunMap,
                          monthMap: calendarDataProvider.monthMap,
                          weekMap: calendarDataProvider.weekMap,
                          dayMap: calendarDataProvider.dayMap,
                        ),
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
