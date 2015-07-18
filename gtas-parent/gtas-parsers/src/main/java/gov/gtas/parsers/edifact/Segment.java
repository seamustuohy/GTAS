package gov.gtas.parsers.edifact;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Top-level segment class contains the segment name (NAD, UNC, CNT, etc.) and
 * an array of Composites. If the array of Composites is null, the segment is
 * acting as just a "marker" segment, and doesn't contain any data.
 */
public class Segment {
    protected static final Logger logger = LoggerFactory.getLogger(Segment.class);

    private String name;
    protected Composite[] composites;

    @SuppressWarnings("unused")
    private Segment() {
    }

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
