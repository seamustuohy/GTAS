package gov.gtas.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "pnr_message")
public class PnrMessage extends Message {
    private static final long serialVersionUID = 1L;  
    public PnrMessage() { }
    
    @Embedded
    private EdifactMessage edifactMessage;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pnrMessage")
    private Set<Pnr> pnrs = new HashSet<>();
    
    public void addPnr(Pnr pnr) {
        if (this.pnrs == null) {
            this.pnrs = new HashSet<>();
        }
        this.pnrs.add(pnr);
        pnr.setPnrMessage(this);
    }

    public Set<Pnr> getPnrs() {
        return pnrs;
    }
    
    public void setPnrs(Set<Pnr> pnrs) {
        this.pnrs = pnrs;
    }

    public EdifactMessage getEdifactMessage() {
        return edifactMessage;
    }

    public void setEdifactMessage(EdifactMessage edifactMessage) {
        this.edifactMessage = edifactMessage;
    }
}
