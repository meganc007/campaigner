import 'package:flutter/material.dart';
import 'package:frontend/campaign_list_page.dart';

void main() async {
  runApp(const CampaignerApp());
}

class CampaignerApp extends StatelessWidget {
  const CampaignerApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "Campaigner",
      theme: ThemeData(
        primarySwatch: Colors.teal,
        appBarTheme: AppBarTheme(
          backgroundColor: Colors.teal[600],
          elevation: 4,
          titleTextStyle: TextStyle(
            color: Colors.white,
            fontSize: 20,
            fontWeight: FontWeight.bold,
          ),
          iconTheme: IconThemeData(color: Colors.white),
        ),
      ),
      home: const CampaignListPage(),
    );
  }
}
