class ForeignKeyConstraintException implements Exception {
  final String message;
  final String type;

  ForeignKeyConstraintException([this.type = 'item'])
    : message = 'This $type is still in use and cannot be deleted.';

  @override
  String toString() => message;
}
