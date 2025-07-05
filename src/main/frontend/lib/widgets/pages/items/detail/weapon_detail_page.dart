import 'package:flutter/material.dart';
import 'package:frontend/models/items/weapon.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/weapon_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/items/add/add_weapon_page.dart';
import 'package:frontend/widgets/pages/items/edit/weapon_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class WeaponDetailPage extends StatefulWidget {
  final String uuid;
  final Map<int, String> weaponTypeMap;
  final Map<int, String> damageTypeMap;
  final Map<int, String> diceTypeMap;
  const WeaponDetailPage({
    super.key,
    required this.uuid,
    required this.weaponTypeMap,
    required this.damageTypeMap,
    required this.diceTypeMap,
  });

  @override
  State<WeaponDetailPage> createState() => _WeaponDetailPageState();
}

class _WeaponDetailPageState extends State<WeaponDetailPage> {
  late Future<List<Weapon>> _futureWeapons;

  @override
  void initState() {
    super.initState();
    _futureWeapons = fetchWeapons(widget.uuid).then((list) {
      list.sort((a, b) => a.name.compareTo(b.name));
      return list;
    });
    ;
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureWeapons = fetchWeapons(widget.uuid).then((list) {
        list.sort((a, b) => a.name.compareTo(b.name));
        return list;
      });
      ;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Weapons".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureWeapons,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final weapons = snapshot.data!;

            final weaponWidgets = weapons
                .map(
                  (weapon) => DetailSection(
                    title: weapon.name,
                    fields: {
                      "Description": weapon.description ?? 'Unknown',
                      "Rarity": weapon.rarity ?? 'Unknown',
                      "Gold Value":
                          weapon.goldValue != null && weapon.goldValue! > 0
                          ? weapon.goldValue.toString()
                          : '0',
                      "Silver Value":
                          weapon.silverValue != null && weapon.silverValue! > 0
                          ? weapon.silverValue.toString()
                          : '0',
                      "Copper Value":
                          weapon.copperValue != null && weapon.copperValue! > 0
                          ? weapon.copperValue.toString()
                          : '0',
                      "Weight": weapon.weight.toString(),
                      "Weapon Type":
                          widget.weaponTypeMap[weapon.fkWeaponType] ??
                          'Unknown',
                      "Damage Type":
                          widget.damageTypeMap[weapon.fkDamageType] ??
                          'Unknown',
                      "Dice Type":
                          widget.diceTypeMap[weapon.fkDiceType] ?? 'Unknown',
                      "Number of Dice": weapon.numberOfDice.toString(),
                      "Damage Modifier": weapon.damageModifier.toString(),
                      "Magical?":
                          weapon.isMagical != null && weapon.isMagical == true
                          ? 'Yes'
                          : 'No',
                      "Cursed?":
                          weapon.isCursed != null && weapon.isCursed == true
                          ? 'Yes'
                          : 'No',
                      "Notes": weapon.notes ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) =>
                              WeaponEditPage(uuid: widget.uuid, weapon: weapon),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: weapon.name,
                        type: 'weapon',
                        onDelete: () => deleteWeapon(weapon.id),
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
                  children: weaponWidgets
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
        label: "Create Weapon",
        destinationBuilder: (context) => AddWeaponPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
