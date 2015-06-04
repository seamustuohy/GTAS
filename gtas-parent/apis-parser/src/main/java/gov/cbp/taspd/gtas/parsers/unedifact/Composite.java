package gov.cbp.taspd.gtas.parsers.unedifact;

import gov.cbp.taspd.gtas.parsers.unedifact.segments.UNA;

import java.util.Arrays;

public class Composite {
    private String value;
    private Element[] elements;

    public Composite(String s, UNA serviceStrings) {
        String[] tmp = s.split("" + serviceStrings.getComponentDataElementSeparator());
        if (tmp.length == 1) {
            this.value = tmp[0];
        } else if (tmp.length > 1) {
            this.elements = new Element[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                this.elements[i] = new Element(tmp[i]);
            }
        } else {
            // error
        }
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
