import 'package:flutter/material.dart';

class MainNav extends StatelessWidget {
  final String campaignName;
  final int currentIndex;
  final void Function(int) onSelect;

  const MainNav({
    super.key,
    required this.campaignName,
    required this.currentIndex,
    required this.onSelect,
  });

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          DrawerHeader(
            decoration: BoxDecoration(color: const Color(0xFFF5E6C4)),
            child: Text(
              "Menu",
              style: const TextStyle(color: Colors.black, fontSize: 20),
            ),
          ),
          ListTile(
            leading: const Icon(Icons.home),
            title: const Text("Campaign Overview"),
            selected: currentIndex == 0,
            onTap: () {
              onSelect(0);
              Navigator.pop(context); // Close drawer
            },
          ),
          ListTile(
            leading: const Icon(Icons.map),
            title: const Text("Locations"),
            selected: currentIndex == 1,
            onTap: () {
              onSelect(1);
              Navigator.pop(context); // Close drawer
            },
          ),
          ListTile(
            leading: const Icon(Icons.people),
            title: const Text("People"),
            selected: currentIndex == 2,
            onTap: () {
              onSelect(2);
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: const Icon(Icons.backpack),
            title: const Text("Items"),
            selected: currentIndex == 3,
            onTap: () {
              onSelect(3);
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: const Icon(Icons.list),
            title: const Text("Quests"),
            selected: currentIndex == 4,
            onTap: () {
              onSelect(4);
              Navigator.pop(context);
            },
          ),
          ListTile(
            leading: const Icon(Icons.calendar_month),
            title: const Text("Calendar"),
            selected: currentIndex == 5,
            onTap: () {
              onSelect(5);
              Navigator.pop(context);
            },
          ),
        ],
      ),
    );
  }
}
