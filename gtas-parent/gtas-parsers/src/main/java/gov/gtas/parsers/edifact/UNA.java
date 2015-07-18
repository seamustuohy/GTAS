package gov.gtas.parsers.edifact;

/**
 * The UNA segment defines special characters used to separate data elements and
 * to terminate the segment. All examples in this document use the default UNA
 * characters. If the UNA segment is not provided in the transmission, it will
 * be assumed the default delimitation characters are used.
 */
public final class UNA {
    public static final int NUM_UNA_CHARS = 6;
    public static final String DEFAULT_UNA = "UNA:+.? '";
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
