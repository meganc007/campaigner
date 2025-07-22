import 'package:flutter/material.dart';
import 'package:frontend/models/location/terrain.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/locations/terrain_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/locations/add/add_terrain_page.dart';
import 'package:frontend/widgets/pages/locations/edit/terrain_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class TerrainDetailPage extends StatefulWidget {
  const TerrainDetailPage({super.key});

  @override
  State<TerrainDetailPage> createState() => _TerrainDetailPageState();
}

class _TerrainDetailPageState extends State<TerrainDetailPage> {
  late Future<List<Terrain>> _futureTerrains;

  @override
  void initState() {
    super.initState();
    _futureTerrains = fetchTerrains();
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureTerrains = fetchTerrains();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Terrains".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureTerrains,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final placeTypes = snapshot.data!;

            final placeTypeWidgets = placeTypes
                .map(
                  (terrain) => DetailSection(
                    title: terrain.name,
                    fields: {"Description": terrain.description},
                    onEdit: () async {
                      final result = await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => TerrainEditPage(terrain: terrain),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: terrain.name,
                        type: "terrain",
                        onDelete: () => deleteTerrain(terrain.id),
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
                  children: placeTypeWidgets
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
        label: "Create Terrain",
        destinationBuilder: (context) => AddTerrainPage(),
        onReturn: _refreshData,
      ),
    );
  }
}
