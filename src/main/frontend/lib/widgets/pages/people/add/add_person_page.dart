import 'package:flutter/material.dart';

class AddPersonPage extends StatefulWidget {
  final String uuid;
  const AddPersonPage({super.key, required this.uuid});

  @override
  State<AddPersonPage> createState() => _AddPersonPageState();
}

class _AddPersonPageState extends State<AddPersonPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Create Person".toUpperCase())),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: SingleChildScrollView(
          child: Center(child: Text("under construction")),
        ),
      ),
    );
  }
}
