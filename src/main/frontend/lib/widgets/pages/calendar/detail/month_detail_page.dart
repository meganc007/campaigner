import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/month.dart';
import 'package:frontend/services/calendar/month_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/calendar/add/add_month_page.dart';
import 'package:frontend/widgets/pages/calendar/edit/month_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class MonthDetailPage extends StatefulWidget {
  final String uuid;
  const MonthDetailPage({super.key, required this.uuid});

  @override
  State<MonthDetailPage> createState() => _MonthDetailPageState();
}

class _MonthDetailPageState extends State<MonthDetailPage> {
  late Future<List<Month>> _futureMonths;

  @override
  void initState() {
    super.initState();
    _futureMonths = fetchMonths(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  Future<void> _refreshData() async {
    _futureMonths = fetchMonths(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Months".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureMonths,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final months = snapshot.data!;

            final monthWidgets = months
                .map(
                  (month) => DetailSection(
                    title: month.name,
                    fields: {
                      "Description": month.description,
                      "Season": month.season,
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              MonthEditPage(uuid: widget.uuid, month: month),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: month.name,
                        type: "month",
                        onDelete: () => deleteMonth(month.id),
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
                  children: monthWidgets
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
        label: "Create Month",
        destinationBuilder: (context) => AddMonthPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
