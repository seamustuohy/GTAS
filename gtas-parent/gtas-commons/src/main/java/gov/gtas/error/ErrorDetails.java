package gov.gtas.error;

import java.util.List;

public interface ErrorDetails {
	String getFatalErrorCode();
    String getFatalErrorMessage();
    List<String> getWarningMessages();
}
