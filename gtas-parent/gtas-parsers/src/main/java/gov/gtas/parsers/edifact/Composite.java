package gov.gtas.parsers.edifact;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 */
public final class Composite {
    private List<String> elements;

    @SuppressWarnings("unused")
    private Composite() { }

    public Composite(List<String> elements) {
        if (elements != null) {
            this.elements = elements;
        } else {
            this.elements = new ArrayList<>();
        }
    }
    
    public List<String> getElements() {
        return this.elements;
    }
    
    public String getElement(int index) {
        if (index < 0 || index >= elements.size()) {
            return null;
        }
        return this.elements.get(index);
    }
    
    public int numElements() {
        return this.elements.size();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
