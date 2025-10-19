import 'package:collection/collection.dart';
import 'package:flutter/material.dart';
import 'package:frontend/models/people/job.dart';
import 'package:frontend/models/people/job_assignment.dart';
import 'package:frontend/models/people/person.dart';
import 'package:frontend/services/form_helper.dart';
import 'package:frontend/services/people/job_assignment_service.dart';
import 'package:frontend/services/people/job_service.dart';
import 'package:frontend/services/people/person_service.dart';
import 'package:frontend/widgets/reusable/dropdown_description.dart';
import 'package:frontend/widgets/reusable/entity_dropdown.dart';
import 'package:frontend/widgets/reusable/submit_button.dart';

class JobAssignmentEditPage extends StatefulWidget {
  final String uuid;
  final JobAssignment jobAssignment;
  const JobAssignmentEditPage({
    super.key,
    required this.uuid,
    required this.jobAssignment,
  });

  @override
  State<JobAssignmentEditPage> createState() => _JobAssignmentEditPageState();
}

class _JobAssignmentEditPageState extends State<JobAssignmentEditPage> {
  final _formKey = GlobalKey<FormState>();
  bool _isSubmitting = false;
  bool _autoValidate = false;

  List<Person> _people = [];
  List<Job> _jobs = [];

  Person? _selectedPerson;
  Job? _selectedJob;

  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadInitialData();
  }

  @override
  void dispose() {
    super.dispose();
  }

  Future<void> _loadInitialData() async {
    try {
      final results = await Future.wait([
        fetchPeople(widget.uuid),
        fetchJobs(),
      ]);

      setState(() {
        _people = results[0] as List<Person>;
        _jobs = results[1] as List<Job>;

        _people.sort((a, b) => a.name.compareTo(b.name));
        _jobs.sort((a, b) => a.name.compareTo(b.name));

        _selectedPerson = _people.firstWhereOrNull(
          (p) => p.id == widget.jobAssignment.fkPerson,
        );
        _selectedJob = _jobs.firstWhereOrNull(
          (j) => j.id == widget.jobAssignment.fkJob,
        );

        _loading = false;
      });
    } catch (e) {
      _error = e.toString();
      _loading = false;
    }
  }

  Future<void> _submitForm() async {
    setState(() => _autoValidate = true);

    if (!_formKey.currentState!.validate()) return;

    setState(() => _isSubmitting = true);

    final localContext = context;

    final success = await editJobAssignment(
      widget.jobAssignment.id,
      _selectedPerson?.id,
      _selectedJob?.id,
      widget.uuid,
    );

    if (!localContext.mounted) return;
    setState(() => _isSubmitting = false);

    handleSuccessOrFailureOnEdit(
      context: localContext,
      success: success,
      isMounted: localContext.mounted,
      entityName: "Job Assignment",
    );
  }

  @override
  Widget build(BuildContext context) {
    if (_loading) {
      return const Center(child: CircularProgressIndicator());
    }
    if (_error != null) {
      return Center(child: Text("Error: $_error"));
    }
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "Edit Job Assignment #${widget.jobAssignment.id}".toUpperCase(),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: SingleChildScrollView(
          child: Center(
            child: Form(
              key: _formKey,
              autovalidateMode: _autoValidate
                  ? AutovalidateMode.onUserInteraction
                  : AutovalidateMode.disabled,
              child: Column(
                children: [
                  EntityDropdown<Person>(
                    label: "Person",
                    selected: _selectedPerson,
                    options: _people,
                    getLabel: (person) =>
                        "${person.firstName} ${person.lastName}",
                    isOptional: false,
                    onChanged: (value) =>
                        setState(() => _selectedPerson = value),
                  ),
                  const SizedBox(height: 16),
                  if (_selectedPerson != null &&
                      _selectedPerson?.description != null)
                    DropdownDescription(_selectedPerson!.description ?? ""),
                  const SizedBox(height: 16),
                  EntityDropdown<Job>(
                    label: "Job",
                    selected: _selectedJob,
                    options: _jobs,
                    getLabel: (job) => job.name,
                    isOptional: false,
                    onChanged: (value) => setState(() => _selectedJob = value),
                  ),
                  const SizedBox(height: 16),
                  if (_selectedJob != null && _selectedJob?.description != null)
                    DropdownDescription(_selectedJob!.description),
                  const SizedBox(height: 24),
                  SubmitButton(
                    isSubmitting: _isSubmitting,
                    onPressed: _submitForm,
                    label: "Update",
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
