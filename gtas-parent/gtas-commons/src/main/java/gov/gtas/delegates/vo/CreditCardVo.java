package gov.gtas.delegates.vo;

import gov.gtas.model.Pnr;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class CreditCardVo extends BaseVo implements Serializable  {
	
    private String cardType;
    private String number;
	private Date expiration;
    private String accountHolder;
    private Set<Pnr> pnrs = new HashSet<>();
    
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
	public Set<Pnr> getPnrs() {
		return pnrs;
	}
	public void setPnrs(Set<Pnr> pnrs) {
		this.pnrs = pnrs;
	}
    

}
