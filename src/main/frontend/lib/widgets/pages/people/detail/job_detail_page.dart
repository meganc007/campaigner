import 'package:flutter/material.dart';
import 'package:frontend/models/people/job.dart';
import 'package:frontend/services/people/job_service.dart';

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
    return const Placeholder(child: Text("under construction"));
  }
}
