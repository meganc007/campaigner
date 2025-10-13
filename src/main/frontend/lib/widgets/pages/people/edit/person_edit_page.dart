import 'package:flutter/material.dart';
import 'package:frontend/models/people/person.dart';

class PersonEditPage extends StatefulWidget {
  final String uuid;
  final Person person;
  const PersonEditPage({super.key, required this.uuid, required this.person});

  @override
  State<PersonEditPage> createState() => _PersonEditPageState();
}

class _PersonEditPageState extends State<PersonEditPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Edit ${widget.person.name}".toUpperCase())),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: SingleChildScrollView(
          child: Center(child: Text("under construction")),
        ),
      ),
    );
  }
}
