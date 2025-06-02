import 'package:flutter/material.dart';
import 'package:frontend/models/location/continent.dart';
import 'package:frontend/services/continents_service.dart';
import 'package:frontend/widgets/add/add_continent_page.dart';
import 'package:frontend/widgets/details/detail_section.dart';
import 'package:frontend/widgets/edit/continent_edit_page.dart';

class ContinentDetailPage extends StatefulWidget {
  final String uuid;
  const ContinentDetailPage({super.key, required this.uuid});

  @override
  State<ContinentDetailPage> createState() => _ContinentDetailPageState();
}

class _ContinentDetailPageState extends State<ContinentDetailPage> {
  late Future<List<Continent>> _futureContinents;

  @override
  void initState() {
    super.initState();
    _futureContinents = fetchContinents(widget.uuid);
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureContinents = fetchContinents(widget.uuid);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Continents".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureContinents,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              return Center(child: Text('Error: ${snapshot.error}'));
            } else if (!snapshot.hasData) {
              return const Center(child: Text('No data available.'));
            }

            final continents = snapshot.data!;

            final continentWidgets = continents
                .map(
                  (continent) => DetailSection(
                    title: continent.name,
                    fields: {"Description": continent.description},
                    onEdit: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              ContinentEditPage(continentId: continent.id),
                        ),
                      );
                    },
                    onDelete: () async {
                      final confirmed = await showDialog<bool>(
                        context: context,
                        builder: (ctx) => AlertDialog(
                          title: Text('Delete ${continent.name}?'),
                          content: const Text(
                            'Are you sure you want to delete this continent?',
                          ),
                          actions: [
                            TextButton(
                              onPressed: () => Navigator.pop(ctx, false),
                              child: const Text('Cancel'),
                            ),
                            TextButton(
                              onPressed: () => Navigator.pop(ctx, true),
                              child: const Text('Delete'),
                            ),
                          ],
                        ),
                      );

                      if (confirmed == true) {
                        await deleteContinent(continent.id);
                        _refreshData();
                      }
                    },
                  ),
                )
                .toList();

            return SafeArea(
              child: Center(
                child: ListView(
                  padding: const EdgeInsets.all(12),
                  children: continentWidgets
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
      bottomNavigationBar: Padding(
        padding: const EdgeInsets.all(16.0),
        child: SizedBox(
          width: double.infinity,
          child: OutlinedButton(
            style: OutlinedButton.styleFrom(
              shape: const RoundedRectangleBorder(
                borderRadius: BorderRadius.zero,
              ),
              side: const BorderSide(
                width: 2,
                color: Color.fromRGBO(93, 64, 55, 1),
              ),
              padding: const EdgeInsets.symmetric(vertical: 16),
            ),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => AddContinentPage(uuid: widget.uuid),
                ),
              );
            },
            child: Text(
              "Create Continent".toUpperCase(),
              style: const TextStyle(color: Colors.black),
            ),
          ),
        ),
      ),
    );
  }
}
