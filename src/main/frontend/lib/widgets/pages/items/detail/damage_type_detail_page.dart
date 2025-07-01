import 'package:flutter/material.dart';
import 'package:frontend/models/items/damage_type.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/damage_type_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/items/add/add_damage_type_page.dart';
import 'package:frontend/widgets/pages/items/edit/damage_type_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class DamageTypeDetailPage extends StatefulWidget {
  const DamageTypeDetailPage({super.key});

  @override
  State<DamageTypeDetailPage> createState() => _DamageTypeDetailPageState();
}

class _DamageTypeDetailPageState extends State<DamageTypeDetailPage> {
  late Future<List<DamageType>> _futureDamageTypes;

  @override
  void initState() {
    super.initState();
    _futureDamageTypes = fetchDamageTypes();
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureDamageTypes = fetchDamageTypes();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Damage Types".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureDamageTypes,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final damageTypes = snapshot.data!;

            final damageTypeWidgets = damageTypes
                .map(
                  (damageType) => DetailSection(
                    title: damageType.name,
                    fields: {
                      "Description": damageType.description ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              DamageTypeEditPage(damageType: damageType),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: damageType.name,
                        type: "damage type",
                        onDelete: () => deleteDamageType(damageType.id),
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
                  children: damageTypeWidgets
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
        label: "Create Damage Type",
        destinationBuilder: (context) => AddDamageTypePage(),
        onReturn: _refreshData,
      ),
    );
  }
}
