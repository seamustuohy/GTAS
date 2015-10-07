package gov.gtas.error;


public interface ErrorDetails {
	Long getErrorId();
	String getErrorCode();
    String getErrorDescription();
    String[] getErrorDetails();
}
