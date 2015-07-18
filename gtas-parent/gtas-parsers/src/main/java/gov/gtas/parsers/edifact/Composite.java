package gov.gtas.parsers.edifact;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A composite may either contain a single value or a collection of elements,
 * but not both.
 * 
 * For example, the segment text "NAD+MS+MIKE" would give us 3 single-value
 * composites. The element array for each of these composites would be null.
 * 
 * "NAD+MS:MIKE" however would give us two composites. The value of the first
 * composites is "NAD", but the value for the second is null. The second
 * composite would have an array of 2 elements ['MS', 'MIKE'].
 */
public final class Composite {
    private String value;
    private Element[] elements;

    @SuppressWarnings("unused")
    private Composite() {
    }

    public Composite(String value) {
        this.value = value;
    }

    public Composite(Element[] elements) {
        this.elements = elements;
    }

    public String getValue() {
        return value;
    }

    public Element[] getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
