package gov.gtas.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "credit_card")
public class CreditCard extends BaseEntityAudit {
    private static final long serialVersionUID = 1L;  
    public CreditCard() { }
    
	@Column(name = "card_type")
    private String cardType;
	
	@Column(nullable = false)
    private String number;
	
    @Temporal(TemporalType.DATE)
    private Date expiration;
	
	@Column(name = "account_holder")
    private String accountHolder;

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.number, this.accountHolder);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CreditCard other = (CreditCard) obj;
        return Objects.equals(this.number, other.number) 
                && Objects.equals(this.accountHolder, other.accountHolder);
    }    		
}
