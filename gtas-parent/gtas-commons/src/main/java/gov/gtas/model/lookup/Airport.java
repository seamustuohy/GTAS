package gov.gtas.model.lookup;

import gov.gtas.model.BaseEntity;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "airport")
public class Airport extends BaseEntity {
    public Airport() { }
    
    private String name;
    
    @Column(length=3)
    private String iata;
    
    @Column(length=4)
    private String icao;
    
    @ManyToOne
    @JoinColumn(referencedColumnName="id")     
    private Country country;
    
    private String city;
    
    @Column(precision = 9, scale = 6 )
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6 )
    private BigDecimal longitude;
    
    @Column(name = "utc_offset")
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
    public Country getCountry() {
        return country;
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
        return Objects.hash(this.iata, this.icao);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Airport other = (Airport) obj;
        return Objects.equals(this.iata, other.iata)
                && Objects.equals(this.icao, other.icao);
    }
    
    public static Airport getByIataCode(String code) {
        return null;
    }
}
