package gov.cbp.taspd.gtas.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Airport extends BaseEntity {
    public Airport() { }
    
    private String name;
    private String iata;
    private String icao;
    private String countryCode;
    private String city;
    
    @Column (precision = 9, scale = 6 )
    private BigDecimal latitude;

    @Column (precision = 9, scale = 6 )
    private BigDecimal longitude;
    private Integer utcOffset;

    public String getName() {
        return name;
    }
    public String getIata() {
        return iata;
    }
    public String getIcao() {
        return icao;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public String getCity() {
        return city;
    }
    public BigDecimal getLatitude() {
        return latitude;
    }
    public BigDecimal getLongitude() {
        return longitude;
    }
    public Integer getUtcOffset() {
        return utcOffset;
    }
    public String getTimezone() {
        return timezone;
    }
    private String timezone;
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((iata == null) ? 0 : iata.hashCode());
        result = prime * result + ((icao == null) ? 0 : icao.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Airport other = (Airport) obj;
        if (iata == null) {
            if (other.iata != null)
                return false;
        } else if (!iata.equals(other.iata))
            return false;
        if (icao == null) {
            if (other.icao != null)
                return false;
        } else if (!icao.equals(other.icao))
            return false;
        return true;
    }
    
    public static Airport getByIataCode(String code) {
        return null;
    }
}
