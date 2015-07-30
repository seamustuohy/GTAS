package gov.gtas.parsers.edifact;

import java.util.ArrayList;
import java.util.List;

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
    private List<Composite> composites;

    @SuppressWarnings("unused")
    private Segment() { }

    /**
     * If the array of Composites is null, the segment is acting as just a
     * "marker" segment, and doesn't contain any data.
     */
    public Segment(String name, List<Composite> composites) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        
        this.name = name;
        if (composites != null) {
            this.composites = composites;            
        } else {
            this.composites = new ArrayList<>();
        }
    }

    public String getName() {
        return this.name;
    }

    public List<Composite> getComposites() {
        return this.composites;
    }

    public Composite getComposite(int index) {
        if (index < 0 || index >= this.composites.size()) {
            return null;
        }
        return this.composites.get(index);
    }
    
    public int numComposites() {
        return this.composites.size();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
