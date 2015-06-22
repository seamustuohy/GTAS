package gov.gtas.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="I")
public class PaxInTransit extends Traveler {

}
