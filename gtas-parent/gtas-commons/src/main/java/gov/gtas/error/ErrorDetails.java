package gov.gtas.error;


public interface ErrorDetails {
	String getErrorId();
	String getFatalErrorCode();
    String getFatalErrorMessage();
    String[] getErrorDetails();
}
