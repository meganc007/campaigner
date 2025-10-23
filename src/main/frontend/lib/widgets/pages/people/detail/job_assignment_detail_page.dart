import 'package:flutter/material.dart';
import 'package:frontend/models/people/job_assignment.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/job_assignment_service.dart';
import 'package:frontend/services/overviews/people_overview_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/people/add/add_job_assignment_page.dart';
import 'package:frontend/widgets/pages/people/edit/job_assignment_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

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
    return Scaffold(
      appBar: AppBar(title: Text("Job Assignments".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureJobAssignments,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final jobAssignments = snapshot.data!;

            final jobAssignmentsWidgets = jobAssignments
                .map(
                  (ja) => DetailSection(
                    title: "#${ja.id}",
                    fields: {
                      "Person": ja.personName ?? 'Unknown',
                      "Job": ja.jobName ?? 'Unknown',
                    },
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => JobAssignmentEditPage(
                            uuid: widget.uuid,
                            jobAssignment: ja,
                          ),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: showJobAssignmentName(ja),
                        type: "job assignment",
                        onDelete: () => deleteJobAssignment(ja.id),
                        onSuccess: _refreshData,
                      );
                    },
                  ),
                )
                .toList();

            return SafeArea(
              child: Center(
                child: ListView(
                  padding: const EdgeInsets.all(12),
                  children: jobAssignmentsWidgets
                      .map(
                        (widget) => Padding(
                          padding: const EdgeInsets.only(bottom: 12),
                          child: widget,
                        ),
                      )
                      .toList(),
                ),
              ),
            );
          },
        ),
      ),
      bottomNavigationBar: CreateNewButton(
        label: "Create Job Assignment",
        destinationBuilder: (context) =>
            AddJobAssignmentPage(uuid: widget.uuid),
        onReturn: _refreshData,
      ),
    );
  }
}
