import 'package:flutter/material.dart';
import 'package:frontend/widgets/campaign_list_page.dart';

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
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFFF5E6C4)),
        useMaterial3: true,
        appBarTheme: AppBarTheme(
          backgroundColor: const Color(0xFFF5E6C4),
          elevation: 4,
          titleTextStyle: TextStyle(
            color: Colors.black,
            fontSize: 20,
            fontWeight: FontWeight.bold,
            letterSpacing: 1.25,
          ),
          iconTheme: IconThemeData(color: Colors.black),
        ),
      ),
      home: const CampaignListPage(),
    );
  }
}
