import 'package:flutter/material.dart';
import 'package:frontend/models/location/region.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/locations/region_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/locations/add/add_region_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';
import 'package:frontend/widgets/pages/locations/edit/region_edit_page.dart';

class RegionDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> countryMap;
  final Map<int, String> climateMap;
  const RegionDetailPage({
    super.key,
    required this.uuid,
    required this.countryMap,
    required this.climateMap,
  });

  @override
  State<RegionDetailPage> createState() => _RegionDetailPageState();
}

class _RegionDetailPageState extends State<RegionDetailPage> {
  late Future<List<Region>> _futureRegions;

  @override
  void initState() {
    super.initState();
    _futureRegions = fetchRegions(widget.uuid);
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureRegions = fetchRegions(widget.uuid);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Regions".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureRegions,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final regions = snapshot.data!;

            final regionWidgets = regions
                .map(
                  (region) => DetailSection(
                    title: region.name,
                    fields: {
                      "Description": region.description,
                      "Country":
                          widget.countryMap[region.fkCountry] ?? 'Unknown',
                      "Climate":
                          widget.climateMap[region.fkClimate] ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              RegionEditPage(uuid: widget.uuid, region: region),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: region.name,
                        type: "region",
                        onDelete: () => deleteRegion(region.id),
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
                  children: regionWidgets
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
        label: "Create Region",
        destinationBuilder: (context) => AddRegionPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
