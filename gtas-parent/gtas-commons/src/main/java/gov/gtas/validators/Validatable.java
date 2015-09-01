package gov.gtas.validators;

/**
 * interface Validatable
 * classes that require validation need to implement this interface and override its 
 * validate method where all the validation logic such as checking for null values ,max.min lengths etc.
 * Parser produces value objects which need validation before store them in database to avoid database 
 * exceptions and failures. 
 * @author GTAS4
 *
 */
public interface Validatable {
	public boolean validate();
}
