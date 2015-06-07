package gov.cbp.taspd.gtas.parsers.unedifact;

import java.util.Arrays;

public class Composite {
    private String value;
    private Element[] elements;

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
        return "Composite [value=" + value + ", elements="
                + Arrays.toString(elements) + "]";
    }
}
