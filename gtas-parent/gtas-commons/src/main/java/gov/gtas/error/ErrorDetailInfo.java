package gov.gtas.error;


public interface ErrorDetailInfo {
	Long getErrorId();
	String getErrorCode();
    String getErrorDescription();
    String getErrorTimestamp();
    String[] getErrorDetails();
}
