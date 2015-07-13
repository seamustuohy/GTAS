package gov.gtas.error;

import java.util.List;

public interface ErrorDetails {
	String getErrorId();
	String getFatalErrorCode();
    String getFatalErrorMessage();
    List<String> getWarningMessages();
}
