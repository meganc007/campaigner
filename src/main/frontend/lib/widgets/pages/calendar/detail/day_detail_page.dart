import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/day.dart';
import 'package:frontend/services/calendar/day_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/calendar/add/add_day_page.dart';
import 'package:frontend/widgets/pages/calendar/edit/day_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class DayDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> weekMap;
  const DayDetailPage({super.key, required this.uuid, required this.weekMap});

  @override
  State<DayDetailPage> createState() => _DayDetailPageState();
}

class _DayDetailPageState extends State<DayDetailPage> {
  late Future<List<Day>> _futureDays;

  @override
  void initState() {
    super.initState();
    _futureDays = fetchDays(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  Future<void> _refreshData() async {
    _futureDays = fetchDays(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Days".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureDays,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final days = snapshot.data!;

            final dayWidgets = days
                .map(
                  (day) => DetailSection(
                    title: day.name,
                    fields: {
                      "Description": day.description,
                      "Week": widget.weekMap[day.fkWeek] ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              DayEditPage(uuid: widget.uuid, day: day),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: day.name,
                        type: "day",
                        onDelete: () => deleteDay(day.id),
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
                  children: dayWidgets
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
        label: "Create Day",
        destinationBuilder: (context) => AddDayPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
