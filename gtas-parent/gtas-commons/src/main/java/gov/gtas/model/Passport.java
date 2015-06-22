package gov.gtas.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="P")
public class Passport extends Document {

}
