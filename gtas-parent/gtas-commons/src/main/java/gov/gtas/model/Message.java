package gov.gtas.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "message")
@Inheritance(strategy = InheritanceType.JOINED)  
public class Message extends BaseEntity {
    
    @Lob
    private byte[] raw;
    
    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "hash_code")
    private String hashCode;
    
    public byte[] getRaw() {
        return raw;
    }
    public void setRaw(byte[] raw) {
        this.raw = raw;
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
