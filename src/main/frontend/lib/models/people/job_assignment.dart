class JobAssignment {
  final int id;
  final int? fkPerson;
  final int? fkJob;
  final String fkCampaignUuid;

  const JobAssignment({
    required this.id,
    this.fkPerson,
    this.fkJob,
    required this.fkCampaignUuid,
  });

  @override
  String toString() {
    return 'JobAssignment(id: $id, fkPerson: $fkPerson, fkJob: $fkJob, fkCampaignUuid: $fkCampaignUuid)';
  }

  factory JobAssignment.fromJson(Map<String, dynamic> json) {
    try {
      return JobAssignment(
        id: json['id'],
        fkPerson: json['fk_person'],
        fkJob: json['fk_job'],
        fkCampaignUuid: json['fk_campaign_uuid'],
      );
    } catch (e) {
      throw FormatException('JobAssignment parse error: $e');
    }
  }
}
