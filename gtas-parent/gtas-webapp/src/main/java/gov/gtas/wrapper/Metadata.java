package gov.gtas.wrapper;

import com.google.gson.annotations.Expose;

public class Metadata {

	@Expose
	private String title;
	@Expose
	private String description;
	@Expose
	private String startDate;
	@Expose
	private String endDate;
	@Expose
	private String enable;
	@Expose
	private String highpriority;
	@Expose
	private String hitsharing;

	/**
	 * 
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 *            The title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            The description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return The startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * 
	 * @param startDate
	 *            The startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * 
	 * @return The endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * 
	 * @param endDate
	 *            The endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * 
	 * @return The enable
	 */
	public String getEnable() {
		return enable;
	}

	/**
	 * 
	 * @param enable
	 *            The enable
	 */
	public void setEnable(String enable) {
		this.enable = enable;
	}

	/**
	 * 
	 * @return The highpriority
	 */
	public String getHighpriority() {
		return highpriority;
	}

	/**
	 * 
	 * @param highpriority
	 *            The highpriority
	 */
	public void setHighpriority(String highpriority) {
		this.highpriority = highpriority;
	}

	/**
	 * 
	 * @return The hitsharing
	 */
	public String getHitsharing() {
		return hitsharing;
	}

	/**
	 * 
	 * @param hitsharing
	 *            The hitsharing
	 */
	public void setHitsharing(String hitsharing) {
		this.hitsharing = hitsharing;
	}

}
