package com.mcommings.campaigner;

public enum ErrorMessage {
    NULL_OR_EMPTY("Item name cannot be null or empty."),
    NAME_EXISTS("Item already exists."),
    ID_NOT_FOUND("Unable to find item with that id."),
    DELETE_NOT_FOUND("Unable to delete; This item was not found."),
    DELETE_FOREIGN_KEY("Unable to delete; This item is referenced in another table."),
    UPDATE_NOT_FOUND("Unable to update; This item was not found."),
    RH_UNABLE_TO_PROCESS_FOREIGN_KEY_LOOKUP("There was an error when attempting to find the foreign keys. Please check that the correct number of repositories was given.");

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
