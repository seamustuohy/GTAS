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
public class WatchlistSpec implements Serializable {
	private static final long serialVersionUID = -182544361080662L;

	private String name;
	private String entity;
	private List<WatchlistItemSpec> watchlistItems;

	public WatchlistSpec() {
	}

	public WatchlistSpec(String name, String entity) {
		this.name = name;
		this.entity = entity;
		watchlistItems = new LinkedList<WatchlistItemSpec>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @return the watchlistItems
	 */
	public List<WatchlistItemSpec> getWatchlistItems() {
		return watchlistItems;
	}

	/**
	 * @param watchlistItems
	 *            the watchlistItems to set
	 */
	public void addWatchlistItem(WatchlistItemSpec watchlistItem) {
		this.watchlistItems.add(watchlistItem);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
