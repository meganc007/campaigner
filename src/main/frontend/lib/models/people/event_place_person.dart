class EventPlacePerson {
  final int id;
  final int? fkEvent;
  final int? fkPlace;
  final int? fkPerson;
  final String fkCampaignUuid;

  const EventPlacePerson({
    required this.id,
    this.fkEvent,
    this.fkPlace,
    this.fkPerson,
    required this.fkCampaignUuid,
  });

  @override
  String toString() {
    return 'EventPlacePerson(id: $id, fkEvent: $fkEvent, fkPlace: $fkPlace, fkPerson: $fkPerson, fkCampaignUuid: $fkCampaignUuid)';
  }

  factory EventPlacePerson.fromJson(Map<String, dynamic> json) {
    try {
      return EventPlacePerson(
        id: json['id'],
        fkEvent: json['fk_event'],
        fkPlace: json['fk_place'],
        fkPerson: json['fk_person'],
        fkCampaignUuid: json['fk_campaign_uuid'],
      );
    } catch (e) {
      throw FormatException('EventPlacePerson parse error: $e');
    }
  }
}
