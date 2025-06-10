import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';

class DropdownDescription extends StatelessWidget {
  final String description;
  final Color? color;
  final String? linkText;
  final VoidCallback? onLinkTap;

  const DropdownDescription(
    this.description, {
    this.color,
    this.linkText,
    this.onLinkTap,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final baseStyle = TextStyle(
      fontFamily: "monospace",
      fontSize: 14,
      color: color ?? Colors.grey[700],
    );

    if (linkText != null && onLinkTap != null) {
      return Text.rich(
        TextSpan(
          style: baseStyle,
          children: [
            TextSpan(text: "$description "),
            TextSpan(
              text: linkText,
              style: baseStyle.copyWith(
                color: Colors.blue,
                decoration: TextDecoration.underline,
              ),
              recognizer: TapGestureRecognizer()..onTap = onLinkTap,
            ),
          ],
        ),
      );
    }

    return Text(description, style: baseStyle);
  }
}
