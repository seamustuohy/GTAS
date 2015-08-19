package gov.gtas.querybuilder.model;

/**
 * 
 * @author GTAS5
 *
 */
public class QueryFlightResult extends BaseQueryResult {

	private String originCountry;
	private String destinationCountry;
	
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public String getDestinationCountry() {
		return destinationCountry;
	}
	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((destinationCountry == null) ? 0 : destinationCountry
						.hashCode());
		result = prime * result
				+ ((originCountry == null) ? 0 : originCountry.hashCode());
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
		QueryFlightResult other = (QueryFlightResult) obj;
		if (destinationCountry == null) {
			if (other.destinationCountry != null)
				return false;
		} else if (!destinationCountry.equals(other.destinationCountry))
			return false;
		if (originCountry == null) {
			if (other.originCountry != null)
				return false;
		} else if (!originCountry.equals(other.originCountry))
			return false;
		return true;
	}
	
}
