import 'package:flutter/material.dart';
import 'package:frontend/models/items/damage_type.dart';
import 'package:frontend/models/items/dice_type.dart';
import 'package:frontend/models/items/inventory.dart';
import 'package:frontend/models/items/item.dart';
import 'package:frontend/models/items/item_type.dart';
import 'package:frontend/models/items/weapon.dart';
import 'package:frontend/models/items/weapon_type.dart';
import 'package:frontend/models/location/place.dart';
import 'package:frontend/models/people/person.dart';
import 'package:frontend/services/items/damage_type_service.dart';
import 'package:frontend/services/items/dice_type_service.dart';
import 'package:frontend/services/items/inventory_service.dart';
import 'package:frontend/services/items/item_service.dart';
import 'package:frontend/services/items/item_type_service.dart';
import 'package:frontend/services/items/weapon_service.dart';
import 'package:frontend/services/items/weapon_type_service.dart';
import 'package:frontend/services/locations/place_service.dart';
import 'package:frontend/services/people/person_service.dart';

class ItemDataProvider extends ChangeNotifier {
  final String uuid;

  ItemDataProvider(this.uuid);

  bool _initialized = false;

  List<Person> _people = [];
  List<Place> _places = [];

  List<Person> get people => List.unmodifiable(_people);
  List<Place> get places => List.unmodifiable(_places);

  List<Inventory> _inventories = [];
  List<Inventory> get inventories => List.unmodifiable(_inventories);

  Map<int, String> _damageTypeMap = {};
  Map<int, String> _diceTypeMap = {};
  Map<int, String> _itemMap = {};
  Map<int, String> _itemTypeMap = {};
  Map<int, String> _weaponMap = {};
  Map<int, String> _weaponTypeMap = {};
  Map<int, String> _personMap = {};
  Map<int, String> _placeMap = {};

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  Map<int, String> get damageTypeMap => Map.unmodifiable(_damageTypeMap);
  Map<int, String> get diceTypeMap => Map.unmodifiable(_diceTypeMap);
  Map<int, String> get itemMap => Map.unmodifiable(_itemMap);
  Map<int, String> get itemTypeMap => Map.unmodifiable(_itemTypeMap);
  Map<int, String> get weaponMap => Map.unmodifiable(_weaponMap);
  Map<int, String> get weaponTypeMap => Map.unmodifiable(_weaponTypeMap);
  Map<int, String> get personMap => Map.unmodifiable(_personMap);
  Map<int, String> get placeMap => Map.unmodifiable(_placeMap);

  Future<void> load() async {
    if (_initialized) return;
    _initialized = true;
    _isLoading = true;
    notifyListeners();

    try {
      final results = await Future.wait([
        fetchDamageTypes(),
        fetchDiceTypes(),
        fetchInventories(uuid),
        fetchItems(uuid),
        fetchItemTypes(),
        fetchWeapons(uuid),
        fetchWeaponTypes(),
        fetchPeople(uuid),
        fetchPlaces(uuid),
      ]);

      final damageTypes = results[0] as List<DamageType>;
      final diceTypes = results[1] as List<DiceType>;
      final inventory = results[2] as List<Inventory>;
      final items = results[3] as List<Item>;
      final itemTypes = results[4] as List<ItemType>;
      final weapons = results[5] as List<Weapon>;
      final weaponTypes = results[6] as List<WeaponType>;
      final people = results[7] as List<Person>;
      final places = results[8] as List<Place>;

      _inventories = inventory;
      _people = people;
      _places = places;

      _damageTypeMap = {for (var dt in damageTypes) dt.id: dt.name};
      _diceTypeMap = {for (var x in diceTypes) x.id: x.name};
      _itemMap = {for (var i in items) i.id: i.name};
      _itemTypeMap = {for (var it in itemTypes) it.id: it.name};
      _weaponMap = {for (var w in weapons) w.id: w.name};
      _weaponTypeMap = {for (var wt in weaponTypes) wt.id: wt.name};
      _personMap = {
        for (var p in people)
          p.id: '${p.firstName ?? ''} ${p.lastName ?? ''}'.trim(),
      };
      _placeMap = {for (var p in places) p.id: p.name};
    } catch (e) {
      _isLoading = false;
      notifyListeners();
      throw Exception('Failed to load item data: $e');
    }

    _isLoading = false;
    notifyListeners();
  }

  void updateDamageType(int id, String name) {
    if (_damageTypeMap[id] != name) {
      _damageTypeMap[id] = name;
      notifyListeners();
    }
  }

  void updateDiceType(int id, String name) {
    if (_diceTypeMap[id] != name) {
      _diceTypeMap[id] = name;
      notifyListeners();
    }
  }

  void updateItem(int id, String name) {
    if (_itemMap[id] != name) {
      _itemMap[id] = name;
      notifyListeners();
    }
  }

  void updateItemType(int id, String name) {
    if (_itemTypeMap[id] != name) {
      _itemTypeMap[id] = name;
      notifyListeners();
    }
  }

  void updateWeapon(int id, String name) {
    if (_weaponMap[id] != name) {
      _weaponMap[id] = name;
      notifyListeners();
    }
  }

  void updateWeaponType(int id, String name) {
    if (_weaponTypeMap[id] != name) {
      _weaponTypeMap[id] = name;
      notifyListeners();
    }
  }
}
