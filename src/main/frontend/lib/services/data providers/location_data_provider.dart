import 'package:flutter/material.dart';
import 'package:frontend/services/locations/city_service.dart';
import 'package:frontend/services/climate_service.dart';
import 'package:frontend/services/locations/continent_service.dart';
import 'package:frontend/services/locations/country_service.dart';
import 'package:frontend/services/government_service.dart';
import 'package:frontend/services/locations/place_type_service.dart';
import 'package:frontend/services/locations/region_service.dart';
import 'package:frontend/services/locations/settlement_type_service.dart';
import 'package:frontend/services/locations/terrain_service.dart';
import 'package:frontend/services/wealth_service.dart';

class LocationDataProvider extends ChangeNotifier {
  final String uuid;

  LocationDataProvider(this.uuid) {
    load();
  }

  Map<int, String> _continentMap = {};
  Map<int, String> _governmentMap = {};
  Map<int, String> _countryMap = {};
  Map<int, String> _climateMap = {};
  Map<int, String> _wealthMap = {};
  Map<int, String> _settlementTypeMap = {};
  Map<int, String> _regionMap = {};
  Map<int, String> _placeTypeMap = {};
  Map<int, String> _terrainMap = {};
  Map<int, String> _cityMap = {};

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  Map<int, String> get continentMap => Map.unmodifiable(_continentMap);
  Map<int, String> get governmentMap => Map.unmodifiable(_governmentMap);
  Map<int, String> get countryMap => Map.unmodifiable(_countryMap);
  Map<int, String> get climateMap => Map.unmodifiable(_climateMap);
  Map<int, String> get wealthMap => Map.unmodifiable(_wealthMap);
  Map<int, String> get settlementTypeMap =>
      Map.unmodifiable(_settlementTypeMap);
  Map<int, String> get regionMap => Map.unmodifiable(_regionMap);
  Map<int, String> get placeTypeMap => Map.unmodifiable(_placeTypeMap);
  Map<int, String> get terrainMap => Map.unmodifiable(_terrainMap);
  Map<int, String> get cityMap => Map.unmodifiable(_cityMap);

  Future<void> load() async {
    _isLoading = true;
    notifyListeners();

    _continentMap = {for (var c in await fetchContinents(uuid)) c.id: c.name};
    _governmentMap = {for (var g in await fetchGovernments()) g.id: g.name};
    _countryMap = {for (var c in await fetchCountries(uuid)) c.id: c.name};
    _climateMap = {for (var c in await fetchClimates()) c.id: c.name};
    _wealthMap = {for (var w in await fetchWealth()) w.id: w.name};
    _settlementTypeMap = {
      for (var st in await fetchSettlementTypes()) st.id: st.name,
    };
    _regionMap = {for (var r in await fetchRegions(uuid)) r.id: r.name};
    _placeTypeMap = {for (var pt in await fetchPlaceTypes()) pt.id: pt.name};
    _terrainMap = {for (var t in await fetchTerrains()) t.id: t.name};
    _cityMap = {for (var c in await fetchCities(uuid)) c.id: c.name};

    _isLoading = false;
    notifyListeners();
  }

  void updateContinent(int id, String name) {
    if (_continentMap[id] != name) {
      _continentMap[id] = name;
      notifyListeners();
    }
  }

  void updateGovernment(int id, String name) {
    if (_governmentMap[id] != name) {
      _governmentMap[id] = name;
      notifyListeners();
    }
  }

  void updateCountry(int id, String name) {
    if (_countryMap[id] != name) {
      _countryMap[id] = name;
      notifyListeners();
    }
  }

  void updateClimate(int id, String name) {
    if (_climateMap[id] != name) {
      _climateMap[id] = name;
      notifyListeners();
    }
  }

  void updateWealth(int id, String name) {
    if (_wealthMap[id] != name) {
      _wealthMap[id] = name;
      notifyListeners();
    }
  }

  void updateSettlementType(int id, String name) {
    if (_settlementTypeMap[id] != name) {
      _settlementTypeMap[id] = name;
      notifyListeners();
    }
  }

  void updatePlaceTypes(int id, String name) {
    if (_placeTypeMap[id] != name) {
      _placeTypeMap[id] = name;
      notifyListeners();
    }
  }

  void updateTerrains(int id, String name) {
    if (_terrainMap[id] != name) {
      _terrainMap[id] = name;
      notifyListeners();
    }
  }

  void updateCities(int id, String name) {
    if (_cityMap[id] != name) {
      _cityMap[id] = name;
      notifyListeners();
    }
  }
}
