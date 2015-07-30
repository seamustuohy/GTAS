package gov.gtas.parsers.edifact;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 */
public final class Composite {
    private String[] elements;

    @SuppressWarnings("unused")
    private Composite() { }

    public Composite(String[] elements) {
        if (elements != null) {
            this.elements = elements;
        } else {
            this.elements = new String[0];
        }
    }
    
    public String getElement(int i) {
        if (i < 0 || i >= elements.length) {
            return null;
        }
        return elements[i];
    }
    
    public int numElements() {
        return elements.length;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
