package gov.gtas.model.watchlist.json;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The base query condition term.
 * @author GTAS3 (AB)
 *
 */
//@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public class WatchlistItem implements Serializable{
	
	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -10797308502449251L;

     private String action;
     private WatchlistTerm[] terms;
     
     public WatchlistItem(){   	 
     }
     public WatchlistItem(String action, WatchlistTerm[] terms){
    	 this.action = action;
    	 this.terms = terms;
     }
      	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the terms
	 */
	public WatchlistTerm[] getTerms() {
		return terms;
	}
	/**
	 * @param terms the terms to set
	 */
	public void setTerms(WatchlistTerm[] terms) {
		this.terms = terms;
	}
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    } 
}
