import 'package:frontend/models/location/city.dart';
import 'package:frontend/models/common/climate.dart';
import 'package:frontend/models/location/continent.dart';
import 'package:frontend/models/location/country.dart';
import 'package:frontend/models/location/landmark.dart';
import 'package:frontend/models/location/place.dart';
import 'package:frontend/models/location/place_type.dart';
import 'package:frontend/models/location/region.dart';
import 'package:frontend/models/location/terrain.dart';

class LocationsOverview {
  final List<Continent> continents;
  final List<Country> countries;
  final List<City> cities;
  final List<Region> regions;
  final List<Landmark> landmarks;
  final List<Place> places;
  final List<PlaceType> placeTypes;
  final List<Terrain> terrains;
  final List<Climate> climates;

  const LocationsOverview({
    required this.continents,
    required this.countries,
    required this.cities,
    required this.regions,
    required this.landmarks,
    required this.places,
    required this.placeTypes,
    required this.terrains,
    required this.climates,
  });

  factory LocationsOverview.fromJson(Map<String, dynamic> json) {
    try {
      return LocationsOverview(
        continents: (json['continents'] as List<dynamic>)
            .map((e) => Continent.fromJson(e as Map<String, dynamic>))
            .toList(),
        countries: (json['countries'] as List<dynamic>)
            .map((e) => Country.fromJson(e as Map<String, dynamic>))
            .toList(),
        cities: (json['cities'] as List<dynamic>)
            .map((e) => City.fromJson(e as Map<String, dynamic>))
            .toList(),
        regions: (json['regions'] as List<dynamic>)
            .map((e) => Region.fromJson(e as Map<String, dynamic>))
            .toList(),
        landmarks: (json['landmarks'] as List<dynamic>)
            .map((e) => Landmark.fromJson(e as Map<String, dynamic>))
            .toList(),
        places: (json['places'] as List<dynamic>)
            .map((e) => Place.fromJson(e as Map<String, dynamic>))
            .toList(),
        placeTypes: (json['placeTypes'] as List<dynamic>)
            .map((e) => PlaceType.fromJson(e as Map<String, dynamic>))
            .toList(),
        terrains: (json['terrains'] as List<dynamic>)
            .map((e) => Terrain.fromJson(e as Map<String, dynamic>))
            .toList(),
        climates: (json['climates'] as List<dynamic>)
            .map((e) => Climate.fromJson(e as Map<String, dynamic>))
            .toList(),
      );
    } catch (e) {
      throw FormatException("Failed to load LocationsOverview: $e");
    }
  }
}
