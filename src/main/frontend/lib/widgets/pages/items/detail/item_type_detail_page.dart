import 'package:flutter/material.dart';
import 'package:frontend/models/items/item_type.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/item_type_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/items/add/add_item_type_page.dart';
import 'package:frontend/widgets/pages/items/edit/item_type_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class ItemTypeDetailPage extends StatefulWidget {
  const ItemTypeDetailPage({super.key});

  @override
  State<ItemTypeDetailPage> createState() => _ItemTypeDetailPageState();
}

class _ItemTypeDetailPageState extends State<ItemTypeDetailPage> {
  late Future<List<ItemType>> _futureItemTypes;

  @override
  void initState() {
    super.initState();
    _futureItemTypes = fetchItemTypes();
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureItemTypes = fetchItemTypes();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Item Types".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureItemTypes,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final itemTypes = snapshot.data!;

            final itemTypeWidgets = itemTypes
                .map(
                  (itemType) => DetailSection(
                    title: itemType.name,
                    fields: {"Description": itemType.description ?? 'Unknown'},
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => ItemTypeEditPage(itemType: itemType),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: itemType.name,
                        type: "item type",
                        onDelete: () => deleteItemType(itemType.id),
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
                  children: itemTypeWidgets
                      .map(
                        (widget) => Padding(
                          padding: EdgeInsets.only(bottom: 12),
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
        label: "Create Item Type",
        destinationBuilder: (context) => AddItemTypePage(),
        onReturn: _refreshData,
      ),
    );
  }
}
