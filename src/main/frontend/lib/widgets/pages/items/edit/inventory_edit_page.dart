import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:frontend/models/items/inventory.dart';
import 'package:frontend/models/items/item.dart';
import 'package:frontend/models/items/weapon.dart';
import 'package:frontend/models/location/place.dart';
import 'package:frontend/models/people/person.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/items/inventory_service.dart';
import 'package:frontend/services/items/item_service.dart';
import 'package:frontend/services/items/weapon_service.dart';
import 'package:frontend/services/locations/place_service.dart';
import 'package:frontend/services/people/person_service.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class InventoryEditPage extends StatefulWidget {
  final String uuid;
  final Inventory inventory;
  const InventoryEditPage({
    super.key,
    required this.uuid,
    required this.inventory,
  });

  @override
  State<InventoryEditPage> createState() => _InventoryEditPageState();
}

class _InventoryEditPageState extends State<InventoryEditPage> {
  final _formKey = GlobalKey<FormState>();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Person> _people = [];
  List<Item> _items = [];
  List<Weapon> _weapons = [];
  List<Place> _places = [];

  Person? _selectedPerson;
  Item? _selectedItem;
  Weapon? _selectedWeapon;
  Place? _selectedPlace;

  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
  }

  @override
  void dispose() {
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([
        fetchPeople(widget.uuid),
        fetchItems(widget.uuid),
        fetchWeapons(widget.uuid),
        fetchPlaces(widget.uuid),
      ]);
      setState(() {
        _people = results[0] as List<Person>;
        _items = results[1] as List<Item>;
        _weapons = results[2] as List<Weapon>;
        _places = results[3] as List<Place>;

        _selectedPerson = _people.firstWhereOrNull(
          (p) => p.id == widget.inventory.fkPerson,
        );
        _selectedItem = _items.firstWhereOrNull(
          (i) => i.id == widget.inventory.fkItem,
        );
        _selectedWeapon = _weapons.firstWhereOrNull(
          (w) => w.id == widget.inventory.fkWeapon,
        );
        _selectedPlace = _places.firstWhereOrNull(
          (pl) => pl.id == widget.inventory.fkPlace,
        );

        _loading = false;
      });
    } catch (e) {
      setState(() {
        _error = e.toString();
        _loading = false;
      });
    }
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    final localContext = context;

    final hasPersonOrPlace = _selectedPerson != null || _selectedPlace != null;
    final hasWeaponOrItem = _selectedWeapon != null || _selectedItem != null;

    if (!hasPersonOrPlace || !hasWeaponOrItem) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            "Please select either a Person or a Place, AND either an Item or a Weapon.",
          ),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }

    final success = await editInventory(
      widget.uuid,
      widget.inventory.id,
      _selectedPerson?.id,
      _selectedItem?.id,
      _selectedWeapon?.id,
      _selectedPlace?.id,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Inventory",
    );
  }

  @override
  Widget build(BuildContext context) {
    if (_loading) {
      return const Center(child: CircularProgressIndicator());
    }
    if (_error != null) {
      return Center(child: Text("Error: $_error"));
    }
    return Scaffold(
      appBar: AppBar(
        title: Text("Edit Inventory #${widget.inventory.id}".toUpperCase()),
      ),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: SingleChildScrollView(
          child: Center(
            child: Form(
              key: _formKey,
              autovalidateMode: _autoValidate
                  ? AutovalidateMode.onUserInteraction
                  : AutovalidateMode.disabled,
              child: Column(
                children: [
                  EntityDropdown<Person>(
                    label: "Person",
                    selected: _selectedPerson,
                    options: _people,
                    getLabel: (person) =>
                        "${person.firstName} ${person.lastName}",
                    isOptional: true,
                    onChanged: (value) =>
                        setState(() => _selectedPerson = value),
                  ),
                  const SizedBox(height: 16),
                  if (_selectedPerson != null &&
                      _selectedPerson?.description != null)
                    DropdownDescription(_selectedPerson!.description ?? ""),
                  const SizedBox(height: 16),
                  EntityDropdown<Item>(
                    label: "Item",
                    selected: _selectedItem,
                    options: _items,
                    getLabel: (item) => item.name,
                    isOptional: true,
                    onChanged: (value) => setState(() => _selectedItem = value),
                  ),
                  const SizedBox(height: 16),
                  if (_selectedItem != null &&
                      _selectedItem?.description != null)
                    DropdownDescription(_selectedItem!.description ?? ""),
                  const SizedBox(height: 16),
                  EntityDropdown<Weapon>(
                    label: "Weapon",
                    selected: _selectedWeapon,
                    options: _weapons,
                    getLabel: (weapon) => weapon.name,
                    isOptional: true,
                    onChanged: (value) =>
                        setState(() => _selectedWeapon = value),
                  ),
                  const SizedBox(height: 16),
                  if (_selectedWeapon != null &&
                      _selectedWeapon?.description != null)
                    DropdownDescription(_selectedWeapon!.description ?? ""),
                  const SizedBox(height: 16),
                  EntityDropdown<Place>(
                    label: "Place",
                    selected: _selectedPlace,
                    options: _places,
                    getLabel: (place) => place.name,
                    isOptional: true,
                    onChanged: (value) =>
                        setState(() => _selectedPlace = value),
                  ),
                  const SizedBox(height: 16),
                  if (_selectedPlace != null &&
                      _selectedPlace?.description != null)
                    DropdownDescription(_selectedPlace!.description),
                  const SizedBox(height: 24),
                  SubmitButton(
                    isSubmitting: _isSubmitting,
                    onPressed: _submitForm,
                    label: "Update",
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
