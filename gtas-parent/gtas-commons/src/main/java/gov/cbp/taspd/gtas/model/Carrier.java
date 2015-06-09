package gov.cbp.taspd.gtas.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Carrier {
    @Id
    String id;
    
    String name;
}
