package gov.gtas.parsers.pnrgov.segment;

public final class UNA {
    public static final int NUM_UNA_CHARS = 6;
    
    private char componentDataElementSeparator;
    private char dataElementSeparator;
    private char decimalMark;
    private char releaseCharacter;
    private char repetitionSeparator;
    private char segmentTerminator;

    public UNA() {
        // use default UNA:+.?*' 
    	//UNA:+.?*'
        componentDataElementSeparator = ':';
        dataElementSeparator = '+';
        decimalMark = '.';
        releaseCharacter = '?';
        repetitionSeparator = '*';
        segmentTerminator = '\'';       
    }
    
    public UNA(String unaSegment) {
        if (unaSegment.length() != 9) {
            throw new IllegalArgumentException("unaSegment.length != 9");
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
