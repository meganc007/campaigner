import 'package:frontend/models/items/damage_type.dart';
import 'package:frontend/models/items/dice_type.dart';
import 'package:frontend/models/items/inventory.dart';
import 'package:frontend/models/items/item.dart';
import 'package:frontend/models/items/item_type.dart';
import 'package:frontend/models/items/weapon.dart';
import 'package:frontend/models/items/weapon_type.dart';

class ItemsOverview {
  final List<DamageType> damageTypes;
  final List<DiceType> diceTypes;
  final List<Inventory> inventories;
  final List<Item> items;
  final List<ItemType> itemTypes;
  final List<Weapon> weapons;
  final List<WeaponType> weaponTypes;

  const ItemsOverview({
    required this.damageTypes,
    required this.diceTypes,
    required this.inventories,
    required this.items,
    required this.itemTypes,
    required this.weapons,
    required this.weaponTypes,
  });

  factory ItemsOverview.fromJson(Map<String, dynamic> json) {
    try {
      return ItemsOverview(
        damageTypes: (json['damageTypes'] as List<dynamic>)
            .map((e) => DamageType.fromJson(e as Map<String, dynamic>))
            .toList(),
        diceTypes: (json['diceTypes'] as List<dynamic>)
            .map((e) => DiceType.fromJson(e as Map<String, dynamic>))
            .toList(),
        inventories: (json['inventories'] as List<dynamic>)
            .map((e) => Inventory.fromJson(e as Map<String, dynamic>))
            .toList(),
        items: (json['items'] as List<dynamic>)
            .map((e) => Item.fromJson(e as Map<String, dynamic>))
            .toList(),
        itemTypes: (json['itemTypes'] as List<dynamic>)
            .map((e) => ItemType.fromJson(e as Map<String, dynamic>))
            .toList(),
        weapons: (json['weapons'] as List<dynamic>)
            .map((e) => Weapon.fromJson(e as Map<String, dynamic>))
            .toList(),
        weaponTypes: (json['weaponTypes'] as List<dynamic>)
            .map((e) => WeaponType.fromJson(e as Map<String, dynamic>))
            .toList(),
      );
    } catch (e) {
      throw FormatException("Failed to load ItemsOverview: $e");
    }
  }
}
