import 'package:flutter/material.dart';
import 'package:frontend/models/people/job.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/job_service.dart';
import 'package:frontend/util/helpers.dart';
import 'package:frontend/widgets/pages/people/add/add_job_page.dart';
import 'package:frontend/widgets/pages/people/edit/job_edit_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/reusable/detail_section.dart';

class JobDetailPage extends StatefulWidget {
  const JobDetailPage({super.key});

  @override
  State<JobDetailPage> createState() => _JobDetailPageState();
}

class _JobDetailPageState extends State<JobDetailPage> {
  late Future<List<Job>> _futureJobs;

  @override
  void initState() {
    super.initState();
    setState(() {
      _futureJobs = fetchJobs().then((list) {
        list.sort((a, b) => a.name.compareTo(b.name));
        return list;
      });
    });
  }

  Future<void> _refreshData() async {
    setState(() {
      _futureJobs = fetchJobs().then((list) {
        list.sort((a, b) => a.name.compareTo(b.name));
        return list;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Jobs".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshData,
        child: FutureBuilder(
          future: _futureJobs,
          builder: (context, snapshot) {
            final validationResult = futureBuilderValidation(snapshot);
            if (validationResult != null) return validationResult;

            final jobs = snapshot.data!;

            final jobWidgets = jobs
                .map(
                  (job) => DetailSection(
                    title: job.name,
                    fields: {"Description": job.description},
                    onEdit: () async {
                      final result = await Navigator.push<bool>(
                        context,
                        MaterialPageRoute(
                          builder: (_) => JobEditPage(job: job),
                        ),
                      );
                      if (result == true) {
                        await _refreshData();
                      }
                    },
                    onDelete: () async {
                      confirmAndDelete(
                        context: context,
                        name: job.name,
                        type: "job",
                        onDelete: () => deleteJob(job.id),
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
                  children: jobWidgets
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
        label: "Create Job",
        destinationBuilder: (context) => AddJobPage(),
        onReturn: _refreshData,
      ),
    );
  }
}
