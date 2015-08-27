package gov.gtas.model.watchlist;

import gov.gtas.model.BaseEntity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "WL_ITEM")
public class WatchlistItem extends BaseEntity {
    private static final long serialVersionUID = 3593L;  
    
    public WatchlistItem() { }

    @ManyToOne
    @JoinColumn(name="ITM_WL_REF", referencedColumnName="ID", nullable = false)     
    private Watchlist watchlist;
    
	@Column(name = "ITM_EDIT_DTTM", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date editTimestamp;

	
	@Column(name = "ITM_DATA", nullable=false, length = 1024)
	private String itemData;
	
    @Column(name = "ITM_RL_DATA", length = 1024)
    private String itemRuleData;
    
    
    /**
	 * @return the watchlist
	 */
	public Watchlist getWatchlist() {
		return watchlist;
	}

	/**
	 * @param watchlist the watchlist to set
	 */
	public void setWatchlist(Watchlist watchlist) {
		this.watchlist = watchlist;
	}

	/**
	 * @return the editTimestamp
	 */
	public Date getEditTimestamp() {
		return editTimestamp;
	}

	/**
	 * @param editTimestamp the editTimestamp to set
	 */
	public void setEditTimestamp(Date editTimestamp) {
		this.editTimestamp = editTimestamp;
	}

	/**
	 * @return the itemData
	 */
	public String getItemData() {
		return itemData;
	}

	/**
	 * @param itemData the itemData to set
	 */
	public void setItemData(String itemData) {
		this.itemData = itemData;
	}

	/**
	 * @return the itemRuleData
	 */
	public String getItemRuleData() {
		return itemRuleData;
	}

	/**
	 * @param itemRuleData the itemRuleData to set
	 */
	public void setItemRuleData(String itemRuleData) {
		this.itemRuleData = itemRuleData;
	}

	@Override
    public int hashCode() {
       return Objects.hash(this.itemData);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final WatchlistItem other = (WatchlistItem)obj;
        return Objects.equals(this.itemData, other.itemData);
    }    
}
