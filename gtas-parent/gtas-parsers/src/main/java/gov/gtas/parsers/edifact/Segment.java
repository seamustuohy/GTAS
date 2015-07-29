package gov.gtas.parsers.edifact;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Top-level segment class contains the segment name (NAD, UNC, CNT, etc.) and
 * an array of Composites. 
 */
public class Segment {
    protected static final Logger logger = LoggerFactory.getLogger(Segment.class);

    private String name;
    private Composite[] composites;

    @SuppressWarnings("unused")
    private Segment() { }

    /**
     * If the array of Composites is null, the segment is acting as just a
     * "marker" segment, and doesn't contain any data.
     */
    public Segment(String name, Composite[] composites) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        
        this.name = name;
        if (composites != null) {
            this.composites = composites;            
        } else {
            this.composites = new Composite[0];
        }
    }

    public String getName() {
        return name;
    }

    public Composite[] getComposites() {
        return composites;
    }

    public Composite getComposite(int i) {
        if (i < 0 || i >= composites.length) {
            return null;
        }
        return composites[i];
    }
    
    public int numComposites() {
        return composites.length;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
