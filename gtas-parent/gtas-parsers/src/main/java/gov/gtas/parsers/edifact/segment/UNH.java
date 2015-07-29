package gov.gtas.parsers.edifact.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * UNH: MESSAGE HEADER
 * <p>
 * A service segment starting and uniquely identifying a message. This tends to
 * vary for different message types, but generally they all contain message type
 * and version, which is all we store here.
 * <p>
 * Example: UNH+MSG001+PAXLST:D:12B:UN:IATA
 */
public class UNH extends Segment {
    private String messageReferenceNumber;
    private String messageType;
    private String messageTypeVersion;
    private String messageTypeReleaseNumber;

    public UNH(Composite[] composites) {
        super(UNH.class.getSimpleName(), composites);
        for (int i = 0; i < this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                this.messageReferenceNumber = c.getValue();
                break;
            case 1:
                Element[] e = c.getElements();
                this.messageType = e[0].getValue();
                this.messageTypeVersion = e[1].getValue();
                this.messageTypeReleaseNumber = e[2].getValue();
                break;
            }
        }
    }

    public String getMessageReferenceNumber() {
        return messageReferenceNumber;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessageTypeVersion() {
        return messageTypeVersion;
    }

    public String getMessageTypeReleaseNumber() {
        return messageTypeReleaseNumber;
    }
}
