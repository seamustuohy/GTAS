package gov.gtas.parsers.edifact;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Edifact Composite class. Consists of zero or more string elements.
 * 
 * @see Segment
 */
public final class Composite {
    private final List<String> elements;

    public Composite(final List<String> elements) {
        if (CollectionUtils.isNotEmpty(elements)) {
            this.elements = elements;
        } else {
            this.elements = new ArrayList<>();
        }
    }
    
    public String getElement(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        } else if (index >= numElements()) {
            return null;
        } else {
            return this.elements.get(index);
        }
    }
    
    public int numElements() {
        return this.elements.size();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
