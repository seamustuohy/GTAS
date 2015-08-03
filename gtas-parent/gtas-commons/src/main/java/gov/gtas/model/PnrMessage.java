package gov.gtas.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "pnr_message")
public class PnrMessage extends Message {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pnrMessage")
    private Set<PnrData> pnrs = new HashSet<>();
    
}
