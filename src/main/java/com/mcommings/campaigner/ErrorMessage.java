package com.mcommings.campaigner;

public enum ErrorMessage {
    NULL_OR_EMPTY("Item name cannot be null or empty."),
    NAME_EXISTS("Item already exists."),
    DELETE_NOT_FOUND("Unable to delete; This item was not found."),
    DELETE_FOREIGN_KEY("Unable to delete; This item is referenced in another table."),
    UPDATE_NOT_FOUND("Unable to update; This item was not found.");

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
