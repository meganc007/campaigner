import 'package:flutter/material.dart';
import 'package:frontend/models/overviews/items_overview.dart';
import 'package:frontend/widgets/reusable/section.dart';
import 'package:frontend/services/data%20providers/item_data_provider.dart';
import 'package:frontend/services/items_overview_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/items/detail/damage_type_detail_page.dart';
import 'package:frontend/widgets/pages/items/detail/dice_type_detail_page.dart';
import 'package:frontend/widgets/pages/items/detail/inventory_detail_page.dart';
import 'package:frontend/widgets/pages/items/detail/item_detail_page.dart';
import 'package:frontend/widgets/pages/items/detail/item_type_detail_page.dart';
import 'package:frontend/widgets/pages/items/detail/weapon_detail_page.dart';
import 'package:frontend/widgets/pages/items/detail/weapon_type_detail_page.dart';
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
    _future = fetchItemsOverview(widget.uuid);

    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<ItemDataProvider>().load();
    });
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
              final validationResult = futureBuilderValidation(snapshot);
              if (validationResult != null) return validationResult;

              final itemOverview = snapshot.data!;

              final Set<String> uniqueNames = itemOverview.inventories
                  .map((inv) => inv.personName ?? inv.placeName ?? '')
                  .where((name) => name.isNotEmpty)
                  .toSet();

              final List<String> sortedNames = uniqueNames.toList()..sort();

              final List<Widget> sectionWidgets = [
                OverviewSection(
                  section: Section(
                    "Items".toUpperCase(),
                    mapNamesToList(itemOverview.items),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => ItemDetailPage(
                          uuid: widget.uuid,
                          itemTypeMap: itemDataProvider.itemTypeMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Item Types".toUpperCase(),
                    mapNamesToList(itemOverview.itemTypes),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => ItemTypeDetailPage()),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Weapons".toUpperCase(),
                    mapNamesToList(itemOverview.weapons),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => WeaponDetailPage(
                          uuid: widget.uuid,
                          weaponTypeMap: itemDataProvider.weaponTypeMap,
                          damageTypeMap: itemDataProvider.damageTypeMap,
                          diceTypeMap: itemDataProvider.diceTypeMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section(
                    "Weapon Types".toUpperCase(),
                    mapNamesToList(itemOverview.weaponTypes),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => WeaponTypeDetailPage()),
                    );
                    await _refreshData();
                  },
                ),
                OverviewSection(
                  section: Section("Inventories".toUpperCase(), sortedNames),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => InventoryDetailPage(
                          uuid: widget.uuid,
                          itemMap: itemDataProvider.itemMap,
                          weaponMap: itemDataProvider.weaponMap,
                        ),
                      ),
                    );
                    await _refreshData();
                  },
                ),
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
                OverviewSection(
                  section: Section(
                    "Dice Types".toUpperCase(),
                    mapNamesToList(itemOverview.diceTypes),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(builder: (_) => DiceTypeDetailPage()),
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
