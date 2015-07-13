package gov.gtas.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "message")
@Inheritance(strategy = InheritanceType.JOINED)  
public class Message extends BaseEntity {
    
    @Lob
    private byte[] raw;
    
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)  
    private Date createDate;

    @Column(name = "hash_code")
    private String hashCode;

    @Enumerated(EnumType.ORDINAL)
    private MessageStatus status;
    
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
    public MessageStatus getStatus() {
        return status;
    }
    public void setStatus(MessageStatus status) {
        this.status = status;
    }    

    @Override
    public int hashCode() {
       return Objects.hash(this.createDate, this.hashCode);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Message other = (Message)obj;
        return Objects.equals(this.createDate, other.createDate)
                && Objects.equals(this.hashCode, other.hashCode);
    }
}
