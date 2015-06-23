package gov.gtas.parsers.edifact;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class Composite {
    private String value;
    private Element[] elements;

    @SuppressWarnings("unused")
    private Composite() { }
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
