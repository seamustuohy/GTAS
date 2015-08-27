package gov.gtas.model.watchlist;

import gov.gtas.enumtype.WatchlistEditEnum;
import gov.gtas.model.BaseEntity;
import gov.gtas.model.User;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "WL_EDIT_LOG")
public class WatchlistEditLog extends BaseEntity {
    private static final long serialVersionUID = 1345L;  
    
    public WatchlistEditLog() { }

    @ManyToOne
    @JoinColumn(name="EDT_AUTHOR", referencedColumnName="user_id", nullable = false)     
    private User editAuthor;
    
    @ManyToOne
    @JoinColumn(name="EDT_WL_REF", referencedColumnName="ID", nullable = false)     
    private Watchlist editedWatchlist;
    
	@Column(name = "EDT_DTTM", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date editTimestamp;

	@Enumerated(EnumType.STRING)
	@Column(name = "EDT_TYPE", nullable=false, length = 1)
	private WatchlistEditEnum editType;
	
	@Column(name = "EDT_DATA_BEFORE", nullable=true, length = 1024)
	private String editDataBefore;

	@Column(name = "EDT_DATA_AFTER", nullable=true, length = 1024)
	private String editDataAfter;	
	

	/**
	 * @return the editAuthor
	 */
	public User getEditAuthor() {
		return editAuthor;
	}

	/**
	 * @param editAuthor the editAuthor to set
	 */
	public void setEditAuthor(User editAuthor) {
		this.editAuthor = editAuthor;
	}

	/**
	 * @return the editedWatchlist
	 */
	public Watchlist getEditedWatchlist() {
		return editedWatchlist;
	}

	/**
	 * @param editedWatchlist the editedWatchlist to set
	 */
	public void setEditedWatchlist(Watchlist editedWatchlist) {
		this.editedWatchlist = editedWatchlist;
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
	 * @return the editType
	 */
	public WatchlistEditEnum getEditType() {
		return editType;
	}

	/**
	 * @param editType the editType to set
	 */
	public void setEditType(WatchlistEditEnum editType) {
		this.editType = editType;
	}

	/**
	 * @return the editDataBefore
	 */
	public String getEditDataBefore() {
		return editDataBefore;
	}

	/**
	 * @param editDataBefore the editDataBefore to set
	 */
	public void setEditDataBefore(String editDataBefore) {
		this.editDataBefore = editDataBefore;
	}

	/**
	 * @return the editDataAfter
	 */
	public String getEditDataAfter() {
		return editDataAfter;
	}

	/**
	 * @param editDataAfter the editDataAfter to set
	 */
	public void setEditDataAfter(String editDataAfter) {
		this.editDataAfter = editDataAfter;
	}

	@Override
    public int hashCode() {
       return Objects.hash(this.editedWatchlist, this.editDataBefore, this.editDataAfter);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final WatchlistEditLog other = (WatchlistEditLog)obj;
        return Objects.equals(this.editedWatchlist, other.editedWatchlist)
                && Objects.equals(this.editDataBefore, other.editDataBefore)
                && Objects.equals(this.editDataAfter, other.editDataAfter);
    }    
}
