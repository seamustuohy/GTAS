package gov.cbp.taspd.gtas.web.model;

/**
 * 
 * @author GTAS5
 *
 */
public class QueryBuilderFlight extends BaseQueryBuilderResult {

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
