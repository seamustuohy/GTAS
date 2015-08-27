package gov.gtas.model.watchlist;

import gov.gtas.enumtype.EntityEnum;
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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "WATCH_LIST",
            uniqueConstraints= {@UniqueConstraint(columnNames={"WL_NAME"})})
public class Watchlist extends BaseEntity {
    private static final long serialVersionUID = 345L;  
    
    public Watchlist() { }
    public Watchlist(String name, EntityEnum entity) { 
    	this.watchlistName = name;
    	this.watchlistEntity = entity;
    }

    @ManyToOne
    @JoinColumn(name="WL_EDITOR", referencedColumnName="user_id", nullable = false)     
    private User watchListEditor;
    	
	@Column(name = "WL_NAME", nullable=false, length = 20)
	private String watchlistName;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "WL_ENTITY", length = 20)
    private EntityEnum watchlistEntity;
    
	@Column(name = "WL_EDIT_DTTM", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date editTimestamp;

    @Column(name = "WL_COMPILE_DTTM", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
    private Date compileTimestamp;
    
    
    /**
	 * @return the watchListEditor
	 */
	public User getWatchListEditor() {
		return watchListEditor;
	}

	/**
	 * @param watchListEditor the watchListEditor to set
	 */
	public void setWatchListEditor(User watchListEditor) {
		this.watchListEditor = watchListEditor;
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
	 * @return the compileTimestamp
	 */
	public Date getCompileTimestamp() {
		return compileTimestamp;
	}

	/**
	 * @param compileTimestamp the compileTimestamp to set
	 */
	public void setCompileTimestamp(Date compileTimestamp) {
		this.compileTimestamp = compileTimestamp;
	}

	/**
	 * @return the watchlistName
	 */
	public String getWatchlistName() {
		return watchlistName;
	}

	/**
	 * @return the watchlistEntity
	 */
	public EntityEnum getWatchlistEntity() {
		return watchlistEntity;
	}

	@Override
    public int hashCode() {
       return super.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Watchlist other = (Watchlist)obj;
        return Objects.equals(this.watchListEditor, other.watchListEditor)
                && Objects.equals(this.watchlistName, other.watchlistName);
    }    
}
