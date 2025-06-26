import 'package:flutter/material.dart';
import 'package:frontend/models/items/inventory.dart';
import 'package:frontend/models/items_overview.dart';
import 'package:frontend/models/section.dart';
import 'package:frontend/services/data%20providers/item_data_provider.dart';
import 'package:frontend/services/items_overview_service.dart';
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

  Map<String, List<Inventory>> groupInventoryByOwner(
    List<Inventory> inventory,
    Map<int, String> personNames,
    Map<int, String> placeNames,
  ) {
    final Map<String, List<Inventory>> grouped = {};

    for (final item in inventory) {
      String ownerKey;
      if (item.fkPerson != null) {
        ownerKey = personNames[item.fkPerson!] ?? 'Unknown Person';
      } else if (item.fkPlace != null) {
        ownerKey = placeNames[item.fkPlace!] ?? 'Unknown Place';
      } else {
        ownerKey = 'Unassigned';
      }

      grouped.putIfAbsent(ownerKey, () => []).add(item);
    }

    return grouped;
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

              final personNames = itemDataProvider.personMap;
              final placeNames = itemDataProvider.placeMap;

              final inventoryByOwner = groupInventoryByOwner(
                itemOverview.inventories,
                personNames,
                placeNames,
              );

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
                        builder: (_) => WeaponDetailPage(uuid: widget.uuid),
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
                  section: Section(
                    "Inventories".toUpperCase(),
                    inventoryByOwner.keys.toList(),
                  ),
                  onSeeMore: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => InventoryDetailPage(uuid: widget.uuid),
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
