package gov.gtas.parsers.edifact;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Segment {
    private String name;
    protected Composite[] composites;

    @SuppressWarnings("unused")
    private Segment() { }

    public Segment(String name, Composite[] composites) {
        this.name = name;
        this.composites = composites;
    }
    
    public String getName() {
        return name;
    }
    public Composite[] getComposites() {
        return composites;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
