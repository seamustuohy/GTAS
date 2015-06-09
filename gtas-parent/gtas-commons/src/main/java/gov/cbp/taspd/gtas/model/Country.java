package gov.cbp.taspd.gtas.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Country {
    @Id
    String id;
    
    String iso2;
    String upperName;
    String name;
    String iso3;
    String iso_numeric;
}
