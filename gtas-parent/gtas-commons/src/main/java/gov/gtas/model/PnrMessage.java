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
    @Embedded
    private EdifactMessage edifactMessage;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pnrMessage")
    private Set<PnrData> pnrs = new HashSet<>();
    
    public Set<PnrData> getPnrs() {
        return pnrs;
    }

    public void setPnrs(Set<PnrData> pnrs) {
        this.pnrs = pnrs;
    }

    public EdifactMessage getEdifactMessage() {
        return edifactMessage;
    }

    public void setEdifactMessage(EdifactMessage edifactMessage) {
        this.edifactMessage = edifactMessage;
    }
}
