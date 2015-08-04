package gov.gtas.model;

public enum MessageStatus {
    RECEIVED,
    PARSED,
    LOADED,
    ANALYZED,
    FAILED_PARSING,
    FAILED_LOADING,
    FAILED_ANALYZING;
}
