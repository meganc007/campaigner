import 'package:flutter/material.dart';
import 'package:frontend/models/items/dice_type.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/dice_type_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/items/add/add_dice_type_page.dart';
import 'package:frontend/widgets/pages/items/edit/dice_type_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class DiceTypeDetailPage extends StatefulWidget {
  const DiceTypeDetailPage({super.key});

  @override
  State<DiceTypeDetailPage> createState() => _DiceTypeDetailPageState();
}

class _DiceTypeDetailPageState extends State<DiceTypeDetailPage> {
  late Future<List<DiceType>> _futureDiceTypes;

  @override
  void initState() {
    super.initState();
    _futureDiceTypes = fetchDiceTypes();
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureDiceTypes = fetchDiceTypes();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Item Types".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureDiceTypes,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final diceType = snapshot.data!;

            final diceTypeWidgets = diceType
                .map(
                  (diceType) => DetailSection(
                    title: diceType.name,
                    fields: {
                      "Description": diceType.description ?? 'Unknown',
                      "Max Roll": diceType.maxRoll.toString(),
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => DiceTypeEditPage(diceType: diceType),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: diceType.name,
                        type: "dice type",
                        onDelete: () => deleteDiceType(diceType.id),
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
                  children: diceTypeWidgets
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
        label: "Create Dice Type",
        destinationBuilder: (context) => AddDiceTypePage(),
        onReturn: _refreshData,
      ),
    );
  }
}
