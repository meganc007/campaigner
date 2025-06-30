import 'package:flutter/material.dart';

Widget? futureBuilderValidation(AsyncSnapshot snapshot) {
  if (snapshot.connectionState == ConnectionState.waiting) {
    return const Center(child: CircularProgressIndicator());
  } else if (snapshot.hasError) {
    return Center(child: Text('Error: ${snapshot.error}'));
  } else if (!snapshot.hasData) {
    return const Center(child: Text('No data available.'));
  } else {
    return null;
  }
}
