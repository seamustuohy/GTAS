package gov.cbp.taspd.gtas.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)  
public class Message  {
    @Id  
//    @GeneratedValue(strategy = GenerationType.AUTO)  
//    @Basic(optional = false)  
//    @Column(name = "id", nullable = false, columnDefinition = "BIGINT UNSIGNED")  
    protected Long id;  
  
    public Long getId() {  
        return id;  
    }  
    
    private String transmissionSource;
    
    @Lob
    private byte[] raw;
    
    private Date transmissionDate;
    private Date createDate;
    private String hashCode;
    
    public String getTransmissionSource() {
        return transmissionSource;
    }
    public void setTransmissionSource(String transmissionSource) {
        this.transmissionSource = transmissionSource;
    }
    public byte[] getRaw() {
        return raw;
    }
    public void setRaw(byte[] raw) {
        this.raw = raw;
    }
    public Date getTransmissionDate() {
        return transmissionDate;
    }
    public void setTransmissionDate(Date transmissionDate) {
        this.transmissionDate = transmissionDate;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getHashCode() {
        return hashCode;
    }
    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
}
