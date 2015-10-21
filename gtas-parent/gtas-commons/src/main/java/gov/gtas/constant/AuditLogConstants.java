package gov.gtas.constant;

public class AuditLogConstants {
    private AuditLogConstants(){}

    // Watchlist logging constants
    public static final String WATCHLIST_LOG_TARGET_PREFIX = "{WLname:";
    public static final String WATCHLIST_LOG_TARGET_SUFFIX = "}";
    public static final String WATCHLIST_LOG_CREATE_MESSAGE = "Watchlist Item created";
    public static final String WATCHLIST_LOG_UPDATE_MESSAGE = "Watchlist Item updated";
    public static final String WATCHLIST_LOG_DELETE_MESSAGE = "Watchlist Item deleted";

    // UDR logging constants
    public static final String UDR_LOG_TARGET_PREFIX = "{UDRtitle:";
    public static final String UDR_LOG_TARGET_SUFFIX = "}";
    public static final String UDR_LOG_CREATE_MESSAGE = "UDR created";
    public static final String UDR_LOG_UPDATE_MESSAGE = "UDR updated";
    public static final String UDR_LOG_UPDATE_META_MESSAGE = "UDR meta data updated";
    public static final String UDR_LOG_DELETE_MESSAGE = "UDR deleted";
}
