import 'package:flutter/material.dart';
import 'package:frontend/campaign.dart';

class CampaignDetailPage extends StatelessWidget {
  final Campaign campaign;
  const CampaignDetailPage({super.key, required this.campaign});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(campaign.name)),
      body: Padding(
        padding: EdgeInsets.all(8.0),
        child: Text("UUID: ${campaign.uuid}"),
      ),
    );
  }
}
