package gov.cbp.taspd.gtas.parsers.unedifact.segments;

public class UNA {
    public char componentDataElementSeparator;
    public char dataElementSeparator;
    public char decimalMark;
    public char releaseCharacter;
    public char repetitionSeparator;
    public char segmentTerminator;

    public UNA() {
        // use default UNA:+.?*' 
        componentDataElementSeparator = ':';
        dataElementSeparator = '+';
        decimalMark = '.';
        releaseCharacter = '?';
        repetitionSeparator = '*';
        segmentTerminator = '\'';       
    }
    
    public UNA(String unaSegment) {
        if (unaSegment.length() != 9) {
            // ?
        }
        componentDataElementSeparator = unaSegment.charAt(3);
        dataElementSeparator = unaSegment.charAt(4);
        decimalMark = unaSegment.charAt(5);
        releaseCharacter = unaSegment.charAt(6);
        repetitionSeparator = unaSegment.charAt(7);
        segmentTerminator = unaSegment.charAt(8);
    }
}
