import 'package:flutter/material.dart';
import 'package:frontend/models/items/item.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/item_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/items/add/add_item_page.dart';
import 'package:frontend/widgets/pages/items/edit/item_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class ItemDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> itemTypeMap;
  const ItemDetailPage({
    super.key,
    required this.uuid,
    required this.itemTypeMap,
  });

  @override
  State<ItemDetailPage> createState() => _ItemDetailPageState();
}

class _ItemDetailPageState extends State<ItemDetailPage> {
  late Future<List<Item>> _futureItems;

  @override
  void initState() {
    super.initState();
    _futureItems = fetchItems(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
    ;
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureItems = fetchItems(widget.uuid).then((list) {
        list.sort((a, b) => a.name.compareTo(b.name));
        return list;
      });
      ;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Items".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureItems,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final items = snapshot.data!;

            final itemWidgets = items
                .map(
                  (item) => DetailSection(
                    title: item.name,
                    fields: {
                      "Description": item.description ?? 'Unknown',
                      "Rarity": item.rarity ?? 'Unknown',
                      "Gold Value":
                          item.goldValue != null && item.goldValue! > 0
                          ? item.goldValue.toString()
                          : '0',
                      "Silver Value":
                          item.silverValue != null && item.silverValue! > 0
                          ? item.silverValue.toString()
                          : '0',
                      "Copper Value":
                          item.copperValue != null && item.copperValue! > 0
                          ? item.copperValue.toString()
                          : '0',
                      "Weight": item.weight.toString(),
                      "Item Type":
                          widget.itemTypeMap[item.fkItemType] ?? 'Unknown',
                      "Magical?":
                          item.isMagical != null && item.isMagical == true
                          ? 'Yes'
                          : 'No',
                      "Cursed?": item.isCursed != null && item.isCursed == true
                          ? 'Yes'
                          : 'No',
                      "Notes": item.notes ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              ItemEditPage(uuid: widget.uuid, item: item),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: item.name,
                        type: "item",
                        onDelete: () => deleteItem(item.id),
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
                  children: itemWidgets
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
        label: "Create Item",
        destinationBuilder: (context) => AddItemPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
