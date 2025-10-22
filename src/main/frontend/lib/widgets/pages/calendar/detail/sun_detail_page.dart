import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/sun.dart';
import 'package:frontend/services/calendar/sun_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/calendar/add/add_sun_page.dart';
import 'package:frontend/widgets/pages/calendar/edit/sun_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class SunDetailPage extends StatefulWidget {
  final String uuid;
  const SunDetailPage({super.key, required this.uuid});

  @override
  State<SunDetailPage> createState() => _SunDetailPageState();
}

class _SunDetailPageState extends State<SunDetailPage> {
  late Future<List<Sun>> _futureSuns;

  @override
  void initState() {
    super.initState();
    _futureSuns = fetchSuns(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  Future<void> _refreshData() async {
    _futureSuns = fetchSuns(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Suns".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureSuns,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final suns = snapshot.data!;

            final sunWidgets = suns
                .map(
                  (sun) => DetailSection(
                    title: sun.name,
                    fields: {"Description": sun.description},
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              SunEditPage(uuid: widget.uuid, sun: sun),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: sun.name,
                        type: "sun",
                        onDelete: () => deleteSun(sun.id),
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
                  children: sunWidgets
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
        label: "Create Sun",
        destinationBuilder: (context) => AddSunPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
