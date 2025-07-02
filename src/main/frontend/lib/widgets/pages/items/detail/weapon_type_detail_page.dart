import 'package:flutter/material.dart';
import 'package:frontend/models/items/weapon_type.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/weapon_type_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/items/add/add_weapon_type_page.dart';
import 'package:frontend/widgets/pages/items/edit/weapon_type_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class WeaponTypeDetailPage extends StatefulWidget {
  const WeaponTypeDetailPage({super.key});

  @override
  State<WeaponTypeDetailPage> createState() => _WeaponTypeDetailPageState();
}

class _WeaponTypeDetailPageState extends State<WeaponTypeDetailPage> {
  late Future<List<WeaponType>> _futureWeaponTypes;

  @override
  void initState() {
    super.initState();
    _futureWeaponTypes = fetchWeaponTypes();
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureWeaponTypes = fetchWeaponTypes();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Weapon Types".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureWeaponTypes,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final weaponTypes = snapshot.data!;

            final weaponTypeWidgets = weaponTypes
                .map(
                  (weaponType) => DetailSection(
                    title: weaponType.name,
                    fields: {
                      "Description": weaponType.description ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              WeaponTypeEditPage(weaponType: weaponType),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: weaponType.name,
                        type: "weapon type",
                        onDelete: () => deleteWeaponType(weaponType.id),
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
                  children: weaponTypeWidgets
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
        label: "Create Weapon Type",
        destinationBuilder: (context) => AddWeaponTypePage(),
        onReturn: _refreshData,
      ),
    );
  }
}
