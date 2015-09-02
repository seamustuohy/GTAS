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
public class WatchlistItemSpec implements Serializable{
	
	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -10797308502449251L;

	 private Long id;
     private String action;
     private WatchlistTerm[] terms;
     
     public WatchlistItemSpec(){   	 
     }
     public WatchlistItemSpec(Long id, String action, WatchlistTerm[] terms){
    	 this.id = id;
    	 this.action = action;
    	 this.terms = terms;
     }
      	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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