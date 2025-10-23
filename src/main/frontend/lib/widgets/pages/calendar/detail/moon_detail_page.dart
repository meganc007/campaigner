import 'package:flutter/material.dart';
import 'package:frontend/models/calendar/moon.dart';
import 'package:frontend/services/calendar/moon_service.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/calendar/add/add_moon_page.dart';
import 'package:frontend/widgets/pages/calendar/edit/moon_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class MoonDetailPage extends StatefulWidget {
  final String uuid;
  const MoonDetailPage({super.key, required this.uuid});

  @override
  State<MoonDetailPage> createState() => _MoonDetailPageState();
}

class _MoonDetailPageState extends State<MoonDetailPage> {
  late Future<List<Moon>> _futureMoons;

  @override
  void initState() {
    super.initState();
    _futureMoons = fetchMoons(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  Future<void> _refreshData() async {
    _futureMoons = fetchMoons(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Moons".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureMoons,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final moons = snapshot.data!;

            final moonWidgets = moons
                .map(
                  (moon) => DetailSection(
                    title: moon.name,
                    fields: {"Description": moon.description},
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              MoonEditPage(uuid: widget.uuid, moon: moon),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: moon.name,
                        type: "moon",
                        onDelete: () => deleteMoon(moon.id),
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
                  children: moonWidgets
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
        label: "Create Moon",
        destinationBuilder: (context) => AddMoonPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
