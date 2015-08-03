package gov.gtas.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "pnr_message")
public class PnrMessage extends Message {
    @Column(name = "message_type")
    private String messageType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pnrMessage")
    private Set<PnrData> pnrs = new HashSet<>();
}
