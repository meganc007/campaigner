import 'package:flutter/material.dart';
import 'package:frontend/models/location/city.dart';
import 'package:frontend/models/location/country.dart';
import 'package:frontend/models/location/place_type.dart';
import 'package:frontend/models/location/region.dart';
import 'package:frontend/models/location/terrain.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/locations/city_service.dart';
import 'package:frontend/services/locations/country_service.dart';
import 'package:frontend/services/locations/place_service.dart';
import 'package:frontend/services/locations/place_type_service.dart';
import 'package:frontend/services/locations/region_service.dart';
import 'package:frontend/services/locations/terrain_service.dart';
import 'package:frontend/widgets/pages/locations/add/add_city_page.dart';
import 'package:frontend/widgets/pages/locations/add/add_country_page.dart';
import 'package:frontend/widgets/pages/locations/add/add_region_page.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/no_associated_entity.dart';
import 'package:frontend/widgets/reusable/no_parent_entity.dart';
import 'package:frontend/widgets/reusable/styled_text_field.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class AddPlacePage extends StatefulWidget {
  final String uuid;
  const AddPlacePage({super.key, required this.uuid});

  @override
  State<AddPlacePage> createState() => _AddPlacePageState();
}

class _AddPlacePageState extends State<AddPlacePage> with RouteAware {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<PlaceType> _placeTypes = [];
  List<Terrain> _terrains = [];
  List<Country> _countries = [];
  List<City> _cities = [];
  List<City> _filteredCities = [];
  List<Region> _regions = [];
  List<Region> _filteredRegions = [];
  PlaceType? _selectedPlaceType;
  Terrain? _selectedTerrain;
  Country? _selectedCountry;
  City? _selectedCity;
  Region? _selectedRegion;
  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
  }

  @override
  void dispose() {
    _nameController.dispose();
    _descriptionController.dispose();
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([
        fetchPlaceTypes(),
        fetchTerrains(),
        fetchCountries(widget.uuid),
        fetchCities(widget.uuid),
        fetchRegions(widget.uuid),
      ]);
      setState(() {
        _placeTypes = results[0] as List<PlaceType>;
        _terrains = results[1] as List<Terrain>;
        _countries = results[2] as List<Country>;
        _cities = results[3] as List<City>;
        _regions = results[4] as List<Region>;

        if (_countries.length == 1) {
          _selectedCountry = _countries.first;
          _filterRegions();
        }

        _loading = false;
      });
    } catch (e) {
      setState(() {
        _error = e.toString();
        _loading = false;
      });
    }
  }

  void _filterCities() {
    setState(() {
      if (_selectedCountry != null) {
        _filteredCities = _cities
            .where((city) => city.fkCountry == _selectedCountry!.id)
            .toList();
      } else {
        _filteredCities = [];
      }
    });
  }

  void _filterRegions() {
    setState(() {
      if (_selectedCountry != null) {
        _filteredRegions = _regions
            .where((region) => region.fkCountry == _selectedCountry!.id)
            .toList();
        if (_filteredRegions.length == 1) {
          _selectedRegion = _filteredRegions.first;
          _filterRegions();
        }
      } else {
        _filteredRegions = [];
      }
    });
  }

  Future<void> _refreshCountries() async {
    final updatedCountries = await fetchCountries(widget.uuid);
    setState(() {
      _countries = updatedCountries;
      if (_countries.length == 1) {
        _selectedCountry = _countries.first;
        _filterRegions();
      }
    });
  }

  Future<void> _refreshRegions() async {
    final updatedRegions = await fetchRegions(widget.uuid);
    setState(() {
      _regions = updatedRegions;
      _filterRegions();
    });
  }

  Future<void> _refreshCities() async {
    final updatedCities = await fetchCities(widget.uuid);
    setState(() {
      _cities = updatedCities;
      _filterCities();
    });
  }

  Future<void> _onCountryChanged(Country? newCountry) async {
    setState(() {
      _selectedCountry = newCountry;
      _selectedRegion = null;
      _selectedCity = null;
      _filterRegions();
      _filterCities();
    });
  }

  Future<void> _onRegionChanged(Region? newRegion) async {
    setState(() {
      _selectedRegion = newRegion;
      _selectedCity = null;

      if (newRegion != null) {
        _filteredCities = _filteredCities
            .where((city) => city.fkRegion == newRegion.id)
            .toList();
      }
    });
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;
    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await createPlace(
      widget.uuid,
      _nameController.text.trim(),
      _descriptionController.text.trim(),
      _selectedPlaceType!.id,
      _selectedTerrain?.id,
      _selectedCountry!.id,
      _selectedCity?.id,
      _selectedRegion!.id,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnCreate(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Place",
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
      appBar: AppBar(title: Text("Create Place".toUpperCase())),
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
                  StyledTextField(
                    controller: _nameController,
                    label: "Name",
                    validator: isNameValid,
                  ),
                  const SizedBox(height: 12),
                  StyledTextField(
                    controller: _descriptionController,
                    label: "Description",
                    maxLines: 3,
                  ),
                  const SizedBox(height: 12),
                  EntityDropdown<PlaceType>(
                    label: "Place Type",
                    selected: _selectedPlaceType,
                    options: _placeTypes,
                    getLabel: (pt) => pt.name,
                    onChanged: (value) =>
                        setState(() => _selectedPlaceType = value),
                  ),
                  const SizedBox(height: 16),
                  if (_selectedPlaceType != null)
                    DropdownDescription(_selectedPlaceType!.description),
                  const SizedBox(height: 12),
                  EntityDropdown<Country>(
                    label: "Country",
                    selected: _selectedCountry,
                    options: _countries,
                    getLabel: (c) => c.name,
                    onChanged: _onCountryChanged,
                  ),
                  const SizedBox(height: 16),
                  NoParentEntity(
                    parents: "countries",
                    show: _countries.isEmpty,
                    onCreateTap: (context) async {
                      final bool? didCreate = await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => AddCountryPage(uuid: widget.uuid),
                        ),
                      );

                      if (didCreate == true) {
                        await _refreshCountries();
                      }
                    },
                  ),
                  if (_selectedCountry != null)
                    DropdownDescription(_selectedCountry!.description),
                  const SizedBox(height: 12),
                  EntityDropdown<Region>(
                    label: "Region",
                    selected: _selectedRegion,
                    options: _filteredRegions,
                    getLabel: (r) => r.name,
                    onChanged: _onRegionChanged,
                  ),
                  const SizedBox(height: 16),
                  NoParentEntity(
                    parents: "regions",
                    show: _regions.isEmpty && _selectedCountry == null,
                    onCreateTap: (context) async {
                      final bool? didCreate = await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => AddRegionPage(uuid: widget.uuid),
                        ),
                      );
                      if (didCreate == true) {
                        await _refreshRegions();
                        await _refreshCountries();
                      }
                    },
                  ),
                  NoAssociatedEntity(
                    show: _selectedCountry != null && _filteredRegions.isEmpty,
                    children: "regions",
                    parent: "country",
                    onCreateTap: (context) async {
                      final bool? didCreate = await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => AddRegionPage(
                            uuid: widget.uuid,
                            preselectedCountry: _selectedCountry!.id,
                          ),
                        ),
                      );
                      if (didCreate == true) {
                        await _refreshRegions();
                      }
                    },
                  ),
                  if (_selectedRegion != null)
                    DropdownDescription(_selectedRegion!.description),
                  const SizedBox(height: 12),
                  EntityDropdown<City>(
                    label: "City",
                    selected: _selectedCity,
                    options: _filteredCities,
                    getLabel: (c) => c.name,
                    onChanged: (value) => setState(() => _selectedCity = value),
                    isOptional: true,
                  ),
                  const SizedBox(height: 16),
                  NoAssociatedEntity(
                    show: _selectedCountry != null && _filteredCities.isEmpty,
                    children: "cities",
                    parent: "country",
                    onCreateTap: (context) async {
                      final bool? didCreate = await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => AddCityPage(
                            uuid: widget.uuid,
                            preselectedCountry: _selectedCountry!.id,
                          ),
                        ),
                      );
                      if (didCreate == true) {
                        await _refreshCities();
                      }
                    },
                  ),
                  if (_selectedCity != null)
                    DropdownDescription(_selectedCity!.description),
                  const SizedBox(height: 12),
                  EntityDropdown<Terrain>(
                    label: "Terrain",
                    selected: _selectedTerrain,
                    options: _terrains,
                    getLabel: (t) => t.name,
                    onChanged: (value) =>
                        setState(() => _selectedTerrain = value),
                    isOptional: true,
                  ),
                  const SizedBox(height: 16),
                  if (_selectedTerrain != null)
                    DropdownDescription(_selectedTerrain!.description),
                  const SizedBox(height: 24),
                  SubmitButton(
                    isSubmitting: _isSubmitting,
                    onPressed: _submitForm,
                    label: "Create",
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
