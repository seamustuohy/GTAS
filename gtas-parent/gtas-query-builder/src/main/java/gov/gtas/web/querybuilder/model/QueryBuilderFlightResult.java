package gov.gtas.web.querybuilder.model;

/**
 * 
 * @author GTAS5
 *
 */
public class QueryBuilderFlightResult extends BaseQueryBuilderResult {

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
	
}
