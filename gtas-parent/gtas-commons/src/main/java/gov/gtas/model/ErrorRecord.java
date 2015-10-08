package gov.gtas.model;

import gov.gtas.error.ErrorDetailInfo;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "error_detail")
public class ErrorRecord extends BaseEntity{
    private static final long serialVersionUID = 19997L; 
    private static final String DETAIL_SEPARATOR = "$$$";
    public ErrorRecord() { }
    public ErrorRecord(ErrorDetailInfo det){
    	this.code = det.getErrorCode();
    	this.description = det.getErrorDescription();
    	this.timestamp = new Date();
    	String[] detList = det.getErrorDetails();
    	if(detList != null && detList.length > 0){
    	    this.details = String.join(DETAIL_SEPARATOR, detList);
    	}
    }
    
    @Column(nullable = false)
	private String code;

    @Column(nullable = false)
	private String description;
    
    @Column(nullable = true)
    @Lob
	private String details;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

    /**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}
	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	public List<String> fetchErrorDetails(){
    	if(this.details != null && this.details.length() > 0){
    		return Arrays.asList(this.details.split(DETAIL_SEPARATOR));
    	} else {
    		return new LinkedList<String>();
    	}
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.timestamp, this.code);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ErrorRecord other = (ErrorRecord) obj;
        return  Objects.equals(this.code, other.code) &&
        		Objects.equals(this.timestamp, other.timestamp);
    }       
}
