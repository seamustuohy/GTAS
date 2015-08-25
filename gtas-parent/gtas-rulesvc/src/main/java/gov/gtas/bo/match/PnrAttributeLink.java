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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + new Long(pnrId).hashCode();
        result = prime * result + new Long(linkAttributeId).hashCode();
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PnrAttributeLink other = (PnrAttributeLink) obj;
        if (pnrId != other.pnrId) {
            return false;
        } 
        if (linkAttributeId != other.linkAttributeId) {
            return false;
        }
        return true;
    }

}
