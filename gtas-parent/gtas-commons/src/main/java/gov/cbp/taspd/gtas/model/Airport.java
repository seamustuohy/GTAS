package gov.cbp.taspd.gtas.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Airport {
    @Id
    private String id;
    
    private String name;
    private String iata;
    private String icao;
    private String countryCode;
    private String city;
    private String latitude;
    private String longitude;
    private Integer utcOffset;
    private String timezone;
}
