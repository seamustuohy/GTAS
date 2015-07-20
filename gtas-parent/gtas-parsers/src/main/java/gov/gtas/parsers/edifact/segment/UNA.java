package gov.gtas.parsers.edifact.segment;

/**
 * <p>
 * UNA: SERVICE STRING ADVICE
 * <p>
 * The Service String Advice (UNA) is Conditional and provides the capability to
 * specify the service characters (delimitation syntax) used within the
 * interchange. The UNA service string advice must be used if the service
 * characters differ from the defaults as identified in ISO 9735 EDIFACT Syntax
 * Rules.
 * <p>
 * The UNA is optional if the default characters are used. When used, the
 * service string advice shall appear immediately before the interchange header
 * segment. The service string advice shall begin with the upper case characters
 * UNA immediately followed by six characters in the order shown below. The same
 * character shall not be used in more than one position of the UNA.
 */
public final class UNA {
    public static final String DEFAULT_UNA = "UNA:+.? '";
    public static final int NUM_UNA_CHARS = 6;
    private static final int TOTAL_UNA_SEGMENT_LENGTH = DEFAULT_UNA.length();

    private char componentDataElementSeparator;
    private char dataElementSeparator;
    private char decimalMark;
    private char releaseCharacter;
    private char repetitionSeparator;
    private char segmentTerminator;

    public UNA() {
        this(DEFAULT_UNA);
    }

    public UNA(String unaSegment) {
        if (unaSegment.length() != TOTAL_UNA_SEGMENT_LENGTH) {
            throw new IllegalArgumentException("una segment length != " + TOTAL_UNA_SEGMENT_LENGTH);
        }
        componentDataElementSeparator = unaSegment.charAt(3);
        dataElementSeparator = unaSegment.charAt(4);
        decimalMark = unaSegment.charAt(5);
        releaseCharacter = unaSegment.charAt(6);
        repetitionSeparator = unaSegment.charAt(7);
        segmentTerminator = unaSegment.charAt(8);
    }

    public char getComponentDataElementSeparator() {
        return componentDataElementSeparator;
    }

    public char getDataElementSeparator() {
        return dataElementSeparator;
    }

    public char getDecimalMark() {
        return decimalMark;
    }

    public char getReleaseCharacter() {
        return releaseCharacter;
    }

    public char getRepetitionSeparator() {
        return repetitionSeparator;
    }

    public char getSegmentTerminator() {
        return segmentTerminator;
    }
}
