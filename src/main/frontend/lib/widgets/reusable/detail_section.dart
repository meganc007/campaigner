import 'package:flutter/material.dart';

class DetailSection extends StatefulWidget {
  final String title;
  final Map<String, String> fields;
  final VoidCallback? onEdit;
  final VoidCallback? onDelete;
  const DetailSection({
    super.key,
    required this.title,
    required this.fields,
    this.onEdit,
    this.onDelete,
  });

  @override
  State<DetailSection> createState() => _DetailSectionState();
}

class _DetailSectionState extends State<DetailSection> {
  bool _expanded = true;

  @override
  Widget build(BuildContext context) {
    return ConstrainedBox(
      constraints: const BoxConstraints(minWidth: 160, maxWidth: 360),
      child: Container(
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          border: Border.all(width: 2, color: Colors.brown.shade700),
          color: const Color(0xFFFDF6E3),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            GestureDetector(
              onTap: () => setState(() => _expanded = !_expanded),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    widget.title,
                    style: const TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 16,
                    ),
                  ),
                  Icon(
                    _expanded
                        ? Icons.keyboard_arrow_up
                        : Icons.keyboard_arrow_down,
                    color: Colors.brown.shade700,
                  ),
                ],
              ),
            ),
            if (_expanded) const Divider(thickness: 1, color: Colors.brown),
            AnimatedCrossFade(
              firstChild: const SizedBox.shrink(),
              secondChild: Padding(
                padding: const EdgeInsets.only(top: 4),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    ...widget.fields.entries.map(
                      (item) => Text(
                        "${item.key}: ${item.value}",
                        style: const TextStyle(
                          fontFamily: "monospace",
                          fontSize: 14,
                        ),
                      ),
                    ),

                    const SizedBox(height: 12),

                    // Buttons Row
                    Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        TextButton(
                          onPressed: widget.onEdit,
                          child: Text('Edit ${widget.title}'),
                        ),
                        Spacer(),
                        TextButton(
                          onPressed: widget.onDelete,
                          child: Text(
                            'Delete ${widget.title}',
                            style: const TextStyle(color: Colors.red),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              crossFadeState: _expanded
                  ? CrossFadeState.showSecond
                  : CrossFadeState.showFirst,
              duration: const Duration(milliseconds: 200),
            ),
          ],
        ),
      ),
    );
  }
}
