import 'package:flutter/material.dart';
import 'package:frontend/models/location/landmark.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/locations/landmark_service.dart';
import 'package:frontend/widgets/pages/locations/add/add_landmark_page.dart';
import 'package:frontend/widgets/pages/locations/edit/landmark_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class LandmarkDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> regionMap;
  const LandmarkDetailPage({
    super.key,
    required this.uuid,
    required this.regionMap,
  });

  @override
  State<LandmarkDetailPage> createState() => _LandmarkDetailPageState();
}

class _LandmarkDetailPageState extends State<LandmarkDetailPage> {
  late Future<List<Landmark>> _futureLandmarks;

  @override
  void initState() {
    super.initState();
    _futureLandmarks = fetchLandmarks(widget.uuid);
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureLandmarks = fetchLandmarks(widget.uuid);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Landmarks".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureLandmarks,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              return Center(child: Text('Error: ${snapshot.error}'));
            } else if (!snapshot.hasData) {
              return const Center(child: Text('No data available.'));
            }

            final landmarks = snapshot.data!;

            final landmarkWidgets = landmarks
                .map(
                  (landmark) => DetailSection(
                    title: landmark.name,
                    fields: {
                      "Description": landmark.description,
                      "Region":
                          widget.regionMap[landmark.fkRegion] ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => LandmarkEditPage(
                            uuid: widget.uuid,
                            landmark: landmark,
                          ),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: landmark.name,
                        type: "landmark",
                        onDelete: () => deleteLandmark(landmark.id),
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
                  children: landmarkWidgets
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
        label: "Create Landmark",
        destinationBuilder: (context) => AddLandmarkPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
