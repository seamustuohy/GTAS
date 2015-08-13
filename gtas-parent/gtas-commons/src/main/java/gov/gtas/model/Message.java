package gov.gtas.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "message")
@Inheritance(strategy = InheritanceType.JOINED)  
public class Message extends BaseEntity {
    private static final long serialVersionUID = 1L;  
    public Message() { }
    
    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)  
    private Date createDate;

    @Column(name = "hash_code", unique = true)
    private String hashCode;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private MessageStatus status;

    @Column(length = 4000)
    private String error;

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
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public MessageStatus getStatus() {
        return status;
    }
    public void setStatus(MessageStatus status) {
        this.status = status;
    }    
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    
    @Override
    public int hashCode() {
       return Objects.hash(this.hashCode);
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
        return Objects.equals(this.hashCode, other.hashCode);
    }
}
