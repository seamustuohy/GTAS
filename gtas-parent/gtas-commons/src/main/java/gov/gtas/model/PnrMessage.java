package gov.gtas.model;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pnr_message")
public class PnrMessage extends Message {
    private static final long serialVersionUID = 1L;  
    public PnrMessage() { }
    
    @Embedded
    private EdifactMessage edifactMessage;

    @ManyToOne(cascade = CascadeType.ALL)
    private Pnr pnr;

    public Pnr getPnr() {
        return pnr;
    }

    public void setPnr(Pnr pnr) {
        this.pnr = pnr;
    }

    public EdifactMessage getEdifactMessage() {
        return edifactMessage;
    }

    public void setEdifactMessage(EdifactMessage edifactMessage) {
        this.edifactMessage = edifactMessage;
    }
}
