package gov.gtas.model.watchlist;

import gov.gtas.enumtype.WatchlistTypeEnum;
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
            uniqueConstraints= {@UniqueConstraint(columnNames={"AUTHOR","WL_NAME"})})
public class Watchlist extends BaseEntity {
    private static final long serialVersionUID = 345L;  
    
    public Watchlist() { }

    @ManyToOne
    @JoinColumn(name="AUTHOR", referencedColumnName="user_id", nullable = false)     
    private User author;
    	
	@Column(name = "WL_NAME", nullable=false, length = 20)
	private String watchlistName;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "WL_TYPE", length = 1)
    private WatchlistTypeEnum watchlistType;
    
	@Column(name = "EDIT_DTTM", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date editTimestamp;

    @Column(name = "WL_COMPILE_DTTM")
	@Temporal(TemporalType.TIMESTAMP)
    private Date compileTimestamp;
    
    
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
        return Objects.equals(this.author, other.author)
                && Objects.equals(this.watchlistName, other.watchlistName);
    }    
}
