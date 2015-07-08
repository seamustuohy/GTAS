package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class EBD to hold Excess Baggage Details of a passenger
 * Used to send paid baggage information
 * @author GTAS4
 *
 * Ex:One piece of baggage over the allowance USD 50 (EBD+USD:50.00+1::N’)
 *
 *
 */
public class EBD extends Segment{
	
	private String currencyCode;
	private String ratePerUnit;
	private String numberInExcess;
	private String pieceOrWeight;
	private String kgsOrPounds;
	

	public EBD(String name, Composite[] composites) {
		super(name, composites);
		// TODO Auto-generated constructor stub
	}


	public String getCurrencyCode() {
		return currencyCode;
	}


	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}


	public String getRatePerUnit() {
		return ratePerUnit;
	}


	public void setRatePerUnit(String ratePerUnit) {
		this.ratePerUnit = ratePerUnit;
	}


	public String getNumberInExcess() {
		return numberInExcess;
	}


	public void setNumberInExcess(String numberInExcess) {
		this.numberInExcess = numberInExcess;
	}


	public String getPieceOrWeight() {
		return pieceOrWeight;
	}


	public void setPieceOrWeight(String pieceOrWeight) {
		this.pieceOrWeight = pieceOrWeight;
	}


	public String getKgsOrPounds() {
		return kgsOrPounds;
	}


	public void setKgsOrPounds(String kgsOrPounds) {
		this.kgsOrPounds = kgsOrPounds;
	}

}
