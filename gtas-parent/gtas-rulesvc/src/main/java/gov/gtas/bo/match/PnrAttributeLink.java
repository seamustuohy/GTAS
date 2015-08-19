package gov.gtas.bo.match;

import java.io.Serializable;

public abstract class PnrAttributeLink implements Serializable {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -7317834427317049240L;
	
	private long pnrId;
	private long linkAttributeId;
	
	protected PnrAttributeLink(final long pnrId, final long attributeId){
		this.pnrId = pnrId;
		this.linkAttributeId = attributeId;
	}

	/**
	 * @return the pnrId
	 */
	public long getPnrId() {
		return pnrId;
	}

	/**
	 * @return the childAttributeId
	 */
	public long getLinkAttributeId() {
		return linkAttributeId;
	}

}
