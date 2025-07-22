import 'package:flutter/material.dart';
import 'package:frontend/models/items/inventory.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/inventory_service.dart';
import 'package:frontend/services/items_overview_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/items/add/add_inventory_page.dart';
import 'package:frontend/widgets/pages/items/edit/inventory_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class InventoryDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> itemMap;
  final Map<int, String> weaponMap;
  const InventoryDetailPage({
    super.key,
    required this.uuid,
    required this.itemMap,
    required this.weaponMap,
  });

  @override
  State<InventoryDetailPage> createState() => _InventoryDetailPageState();
}

class _InventoryDetailPageState extends State<InventoryDetailPage> {
  late Future<List<Inventory>> _futureInventories;

  @override
  void initState() {
    super.initState();
    _futureInventories = fetchInventoriesFromItemsOverview(widget.uuid);
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureInventories = fetchInventoriesFromItemsOverview(widget.uuid);
    });
  }

  String showInventoryName(Inventory inventory) {
    if (inventory.placeName != null) {
      return inventory.placeName!;
    }

    if (inventory.personName != null) {
      return inventory.personName!;
    }

    return "Unknown";
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Inventories".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureInventories,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final inventories = snapshot.data!;
            inventories.sort((a, b) => a.id.compareTo(b.id));

            final inventoryWidgets = inventories
                .map(
                  (i) => DetailSection(
                    title: "#${i.id}",
                    fields: {
                      "Person": i.personName ?? 'Unknown',
                      "Place": i.placeName ?? 'Unknown',
                      "Item": widget.itemMap[i.fkItem] ?? 'Unknown',
                      "Weapon": widget.weaponMap[i.fkWeapon] ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => InventoryEditPage(
                            uuid: widget.uuid,
                            inventory: i,
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
                        name: showInventoryName(i),
                        type: "inventory",
                        onDelete: () => deleteInventory(i.id),
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
                  children: inventoryWidgets
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
        label: "Create Inventory",
        destinationBuilder: (context) => AddInventoryPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
