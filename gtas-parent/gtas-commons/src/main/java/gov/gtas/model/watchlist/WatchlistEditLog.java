package gov.gtas.model.watchlist;

import gov.gtas.constant.DomainModelConstants;
import gov.gtas.enumtype.WatchlistEditEnum;
import gov.gtas.model.BaseEntity;
import gov.gtas.model.User;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "WL_EDIT_LOG")
public class WatchlistEditLog extends BaseEntity {
	private static final long serialVersionUID = 1345L;

	public WatchlistEditLog() {
	}

	public WatchlistEditLog(User editor, Watchlist editedWl,
			WatchlistEditEnum editType, String editData) {
		this.watchListEditor = editor.getUserId();
		this.editedWatchlist = editedWl.getWatchlistName();
		this.editType = editType;
		this.editData = editData;
		this.editTimestamp = new Date();
	}

	@Column(name = "EDT_USERID", nullable = false, length = DomainModelConstants.GTAS_USERID_COLUMN_SIZE)
	private String watchListEditor;

	@Column(name = "EDT_WLNAME", nullable = false, length = DomainModelConstants.WL_NAME_COLUMN_SIZE)
	private String editedWatchlist;

	@Column(name = "EDT_DTTM", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date editTimestamp;

	@Enumerated(EnumType.STRING)
	@Column(name = "EDT_TYPE", nullable = false, length = 1)
	private WatchlistEditEnum editType;

	@Column(name = "EDT_DATA", nullable = false, length = DomainModelConstants.WL_ITEM_DATA_COLUMN_SIZE)
	private String editData;

	/**
	 * @return the watchListEditor
	 */
	public String getWatchListEditor() {
		return watchListEditor;
	}

	/**
	 * @param watchListEditor
	 *            the watchListEditor to set
	 */
	public void setWatchListEditor(String watchListEditor) {
		this.watchListEditor = watchListEditor;
	}


	/**
	 * @return the editedWatchlist
	 */
	public String getEditedWatchlist() {
		return editedWatchlist;
	}

	/**
	 * @param editedWatchlist the editedWatchlist to set
	 */
	public void setEditedWatchlist(String editedWatchlist) {
		this.editedWatchlist = editedWatchlist;
	}

	/**
	 * @return the editTimestamp
	 */
	public Date getEditTimestamp() {
		return editTimestamp;
	}

	/**
	 * @param editTimestamp
	 *            the editTimestamp to set
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
	 * @param editType
	 *            the editType to set
	 */
	public void setEditType(WatchlistEditEnum editType) {
		this.editType = editType;
	}

	/**
	 * @return the editData
	 */
	public String getEditData() {
		return editData;
	}

	/**
	 * @param editData
	 *            the editData to set
	 */
	public void setEditData(String editData) {
		this.editData = editData;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.editedWatchlist, this.editType, this.editData);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WatchlistEditLog other = (WatchlistEditLog) obj;
		return Objects.equals(this.editedWatchlist, other.editedWatchlist)
				&& Objects.equals(this.editType, other.editType)
				&& Objects.equals(this.editData, other.editData);
	}
}
