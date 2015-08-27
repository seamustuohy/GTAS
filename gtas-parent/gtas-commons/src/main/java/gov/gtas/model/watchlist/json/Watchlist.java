package gov.gtas.model.watchlist.json;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Recursive query condition object.
 * 
 * @author GTAS3 (AB)
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Watchlist implements Serializable {
	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -182544361080662L;

	private String entity;
	private List<WatchlistItem> watchlistItems;

	public Watchlist() {
	}

	public Watchlist(String entity) {
		this.entity = entity;
		watchlistItems = new LinkedList<WatchlistItem>();
	}

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @return the watchlistItems
	 */
	public List<WatchlistItem> getWatchlistItems() {
		return watchlistItems;
	}

	/**
	 * @param watchlistItems
	 *            the watchlistItems to set
	 */
	public void addWatchlistItem(WatchlistItem watchlistItem) {
		this.watchlistItems.add(watchlistItem);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
