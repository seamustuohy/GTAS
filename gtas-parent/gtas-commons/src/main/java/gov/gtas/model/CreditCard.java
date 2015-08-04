package gov.gtas.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "credit_card")
public class CreditCard extends BaseEntityAudit {
	@Column(name = "card_type")
    private String cardType;
	
	@Column(name = "card_number")
    private String cardNumber;
	
	@Column(name = "card_expiration")
    private String cardExpiration;
	
	@Column(name = "card_holder_Name")
    private String cardHolderName;
	/*
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pnr_id",referencedColumnName="id")     
    private PnrData pnrData;

*/
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardExpiration() {
		return cardExpiration;
	}

	public void setCardExpiration(String cardExpiration) {
		this.cardExpiration = cardExpiration;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

/*	public PnrData getPnrData() {
		return pnrData;
	}

	public void setPnrData(PnrData pnrData) {
		this.pnrData = pnrData;
	}*/
	
    @Override
    public int hashCode() {
        return Objects.hash(this.cardNumber,this.cardHolderName);
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
        return Objects.equals(this.cardNumber, other.cardNumber);
    }    		
}
