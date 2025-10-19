class EventPlacePerson {
  final int id;
  final int? fkEvent;
  final int? fkPlace;
  final int? fkPerson;
  final String fkCampaignUuid;
  final String? eventName;
  final String? placeName;
  final String? personName;

  const EventPlacePerson({
    required this.id,
    this.fkEvent,
    this.fkPlace,
    this.fkPerson,
    required this.fkCampaignUuid,
    this.eventName,
    this.placeName,
    this.personName,
  });

  @override
  String toString() {
    return 'EventPlacePerson(id: $id, fkEvent: $fkEvent, fkPlace: $fkPlace, fkPerson: $fkPerson, fkCampaignUuid: $fkCampaignUuid, eventName: $eventName, placeName: $placeName, personName: $personName)';
  }

  factory EventPlacePerson.fromJson(Map<String, dynamic> json) {
    try {
      return EventPlacePerson(
        id: json['id'],
        fkEvent: json['fk_event'],
        fkPlace: json['fk_place'],
        fkPerson: json['fk_person'],
        fkCampaignUuid: json['fk_campaign_uuid'],
        eventName: json['eventName'],
        placeName: json['placeName'],
        personName: json['personName'],
      );
    } catch (e) {
      throw FormatException('EventPlacePerson parse error: $e');
    }
  }
}
