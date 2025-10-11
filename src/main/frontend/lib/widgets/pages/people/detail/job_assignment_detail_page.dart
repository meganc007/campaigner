import 'package:flutter/material.dart';
import 'package:frontend/models/people/job.dart';
import 'package:frontend/models/people/job_assignment.dart';
import 'package:frontend/services/people/job_assignment_service.dart';
import 'package:frontend/services/people_overview_service.dart';

class JobAssignmentDetailPage extends StatefulWidget {
  final String uuid;
  const JobAssignmentDetailPage({super.key, required this.uuid});

  @override
  State<JobAssignmentDetailPage> createState() =>
      _JobAssignmentDetailPageState();
}

class _JobAssignmentDetailPageState extends State<JobAssignmentDetailPage> {
  late Future<List<JobAssignment>> _futureJobAssignments;

  @override
  void initState() {
    super.initState();
    setState(() {
      _futureJobAssignments = fetchJobAssignmentsFromPeopleOverview(
        widget.uuid,
      );
    });
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureJobAssignments = fetchJobAssignmentsFromPeopleOverview(
        widget.uuid,
      );
    });
  }

  String showJobAssignmentName(JobAssignment jobAssignment) {
    if (jobAssignment.personName != null) {
      return jobAssignment.personName!;
    }

    if (jobAssignment.jobName != null) {
      return jobAssignment.jobName!;
    }

    return "Unknown";
  }

  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}
