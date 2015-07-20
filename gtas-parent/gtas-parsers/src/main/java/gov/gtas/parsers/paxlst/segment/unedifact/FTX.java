package gov.gtas.parsers.paxlst.segment.unedifact;

import java.text.ParseException;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * FTX: FREE TEXT - GR. 4
 * <p>
 * This segment is optional and may be used to report Bag Tag Identification.
 * <p>
 * Examples:
 * <ol>
 * <li>FTX+BAG+++BA987654’ - Single Bag Tag reference
 * <li>FTX+BAG+++AF012345:3’ - Indicates 3 bags checked beginning with a
 * sequential reference of AF012345.
 * </ol>
 */
public class FTX extends Segment {
    public enum FtxCode {
        BAG
    }

    private FtxCode ftxCode;

    /** This element reports the Bag Tag identification reference */
    private String bagId;

    /**
     * Conditional: This element reports a numeric value indicating a sequence
     * of values in a +1 increment beginning with the value in the previous
     * element.
     */
    private String numBags;

    public FTX(Composite[] composites) throws ParseException {
        super(FTX.class.getSimpleName(), composites);
        for (int i = 0; i < this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                switch (c.getValue()) {
                case "BAG":
                    this.ftxCode = FtxCode.BAG;
                    break;
                }
                break;

            case 3:
                if (e == null) {
                    this.bagId = c.getValue();
                } else {
                    if (e.length >= 1) {
                        this.bagId = e[0].getValue();
                    }
                    if (e.length >= 2) {
                        this.numBags = e[1].getValue();
                    }
                }
                break;
            }
        }
    }

    public FtxCode getFtxCode() {
        return ftxCode;
    }

    public String getBagId() {
        return bagId;
    }

    public String getNumBags() {
        return numBags;
    }
}
