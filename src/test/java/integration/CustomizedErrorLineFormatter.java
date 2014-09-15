package integration;

import format.ErrorLineFormatter;

public class CustomizedErrorLineFormatter implements ErrorLineFormatter {
    @Override
    public String format(int lineNumber, String columnName, String errorMessage) {
        return "Column " + columnName + " in Line " + lineNumber + " has error: " + errorMessage;
    }
}
