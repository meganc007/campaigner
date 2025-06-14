import 'package:flutter/material.dart';
import 'package:frontend/models/items_overview.dart';
import 'package:frontend/models/section.dart';
import 'package:frontend/services/data%20providers/item_data_provider.dart';
import 'package:frontend/services/items_overview_service.dart';
import 'package:frontend/widgets/pages/items/damage_type_detail_page.dart';
import 'package:frontend/widgets/pages/overviews/overview_section.dart';
import 'package:provider/provider.dart';

class ItemsOverviewPage extends StatefulWidget {
  final String uuid;
  final Future<ItemsOverview> futureItems;

  const ItemsOverviewPage({
    super.key,
    required this.uuid,
    required this.futureItems,
  });

  @override
  State<ItemsOverviewPage> createState() => _ItemsOverviewPageState();
}

class _ItemsOverviewPageState extends State<ItemsOverviewPage> {
  late Future<ItemsOverview> _future;

  @override
  void initState() {
    super.initState();
    _future = widget.futureItems;
  }

  Future<void> _refreshData() async {
    setState(() {
      _future = fetchItemsOverview(widget.uuid);
    });

    final provider = context.read<ItemDataProvider>();
    await provider.load();
  }

  List<String> mapNamesToList(List<dynamic> items) {
    return items.map((item) => (item as dynamic).name as String).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<ItemDataProvider>(
      builder: (context, itemDataProvider, child) {
        if (itemDataProvider.isLoading) {
          return const Center(child: CircularProgressIndicator());
        }

        return RefreshIndicator(
          onRefresh: _refreshData,
          child: FutureBuilder<ItemsOverview>(
            future: _future,
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return const Center(child: CircularProgressIndicator());
              } else if (snapshot.hasError) {
                return Center(child: Text('Error: ${snapshot.error}'));
              } else if (!snapshot.hasData) {
                return const Center(child: Text('No data available.'));
              }

              final itemOverview = snapshot.data!;

              final List<Widget> sectionWidgets = [
                OverviewSection(
                  section: Section(
                    "Damage Types".toUpperCase(),
                    mapNamesToList(itemOverview.damageTypes),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => DamageTypeDetailPage()),
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
