package gov.gtas.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="C")
public class Crew extends Traveler {

}
