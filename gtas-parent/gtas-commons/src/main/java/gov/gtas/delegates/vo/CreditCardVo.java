package gov.gtas.delegates.vo;

import gov.gtas.validators.Validatable;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CreditCardVo extends BaseVo implements Validatable  {
	
    private String cardType;
    private String number;
    private Date expiration;
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
	public String toString() {
	        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.number) || StringUtils.isBlank(this.accountHolder) 
				|| StringUtils.isBlank(this.cardType) || this.expiration == null){
			return false;
		}
		return true;
	}    

}
