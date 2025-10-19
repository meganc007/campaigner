import 'package:flutter/material.dart';
import 'package:frontend/models/common/campaign.dart';
import 'package:frontend/widgets/pages/campaign/add_campaign_page.dart';
import 'package:frontend/widgets/reusable/create_new_button.dart';
import 'package:frontend/widgets/pages/campaign/campaign_detail_page.dart';
import 'package:frontend/services/campaign_service.dart';

class CampaignListPage extends StatefulWidget {
  const CampaignListPage({super.key});

  @override
  State<CampaignListPage> createState() => _CampaignListPageState();
}

class _CampaignListPageState extends State<CampaignListPage> {
  late Future<List<Campaign>> _futureCampaigns;

  @override
  void initState() {
    super.initState();
    _futureCampaigns = fetchCampaigns();
  }

  Future<void> _refreshCampaigns() async {
    setState(() {
      _futureCampaigns = fetchCampaigns();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Campaigns".toUpperCase())),
      body: RefreshIndicator(
        onRefresh: _refreshCampaigns,
        child: FutureBuilder<List<Campaign>>(
          future: _futureCampaigns,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              return Center(child: Text("Error: ${snapshot.error}"));
            } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
              return const Center(child: Text("No campaigns found."));
            }
            final campaigns = snapshot.data!;
            return ListView.builder(
              itemCount: campaigns.length,
              itemBuilder: (context, index) {
                final campaign = campaigns[index];
                return ListTile(
                  title: Text(campaign.name),
                  subtitle: Text(campaign.description),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) =>
                            CampaignDetailPage(campaign: campaign),
                      ),
                    );
                  },
                );
              },
            );
          },
        ),
      ),
      bottomNavigationBar: CreateNewButton(
        label: "Create Campaign",
        destinationBuilder: (context) => AddCampaignPage(),
        onReturn: _refreshCampaigns,
      ),
    );
  }
}
