import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/week.dart';
import 'package:frontend/services/calendar/week_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/calendar/add/add_week_page.dart';
import 'package:frontend/widgets/pages/calendar/edit/week_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class WeekDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> monthMap;
  const WeekDetailPage({super.key, required this.uuid, required this.monthMap});

  @override
  State<WeekDetailPage> createState() => _WeekDetailPageState();
}

class _WeekDetailPageState extends State<WeekDetailPage> {
  late Future<List<Week>> _futureWeeks;

  @override
  void initState() {
    super.initState();
    _futureWeeks = fetchWeeks(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  Future<void> _refreshData() async {
    _futureWeeks = fetchWeeks(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Weeks".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureWeeks,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final weeks = snapshot.data!;

            final weekWidgets = weeks
                .map(
                  (week) => DetailSection(
                    title: week.name,
                    fields: {
                      "Description": week.description,
                      "Week": week.weekNumber.toString(),
                      "Month": widget.monthMap[week.fkMonth] ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              WeekEditPage(uuid: widget.uuid, week: week),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: week.name,
                        type: "week",
                        onDelete: () => deleteWeek(week.id),
                        onSuccess: _refreshData,
                      );
                    },
                  ),
                )
                .toList();

            return SafeArea(
              child: Center(
                child: ListView(
                  padding: const EdgeInsets.all(12),
                  children: weekWidgets
                      .map(
                        (widget) => Padding(
                          padding: const EdgeInsets.only(bottom: 12),
                          child: widget,
                        ),
                      )
                      .toList(),
                ),
              ),
            );
          },
        ),
      ),
      bottomNavigationBar: CreateNewButton(
        label: "Create Week",
        destinationBuilder: (context) => AddWeekPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
