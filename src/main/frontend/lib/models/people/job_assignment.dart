class JobAssignment {
  final int id;
  final int? fkPerson;
  final int? fkJob;
  final String fkCampaignUuid;
  final String? personName;
  final String? jobName;

  const JobAssignment({
    required this.id,
    this.fkPerson,
    this.fkJob,
    required this.fkCampaignUuid,
    this.personName,
    this.jobName,
  });

  @override
  String toString() {
    return 'JobAssignment(id: $id, fkPerson: $fkPerson, fkJob: $fkJob, fkCampaignUuid: $fkCampaignUuid, personName: $personName, jobName: $jobName)';
  }

  factory JobAssignment.fromJson(Map<String, dynamic> json) {
    try {
      return JobAssignment(
        id: json['id'],
        fkPerson: json['fk_person'],
        fkJob: json['fk_job'],
        fkCampaignUuid: json['fk_campaign_uuid'],
        personName: json['personName'],
        jobName: json['jobName'],
      );
    } catch (e) {
      throw FormatException('JobAssignment parse error: $e');
    }
  }
}
