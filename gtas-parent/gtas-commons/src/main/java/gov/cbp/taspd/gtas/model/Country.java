package gov.cbp.taspd.gtas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "country")
public class Country extends BaseEntity {
    public Country() { }
    
    @Column(length = 2)
    private String iso2;

    @Column(length = 3)
    private String iso3;
    
    private String name;
    
    @Column(name = "iso_numeric", length = 3)
    private String isoNumeric;
    
    public String getIso2() {
        return iso2;
    }
    public String getIso3() {
        return iso3;
    }
    public String getName() {
        return name;
    }
    public String getIsoNumeric() {
        return isoNumeric;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((iso2 == null) ? 0 : iso2.hashCode());
        result = prime * result + ((iso3 == null) ? 0 : iso3.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Country other = (Country) obj;
        if (iso2 == null) {
            if (other.iso2 != null)
                return false;
        } else if (!iso2.equals(other.iso2))
            return false;
        if (iso3 == null) {
            if (other.iso3 != null)
                return false;
        } else if (!iso3.equals(other.iso3))
            return false;
        return true;
    }
    
    public static Country getByAlpha2Code(String alpha2) {
        return null;
    }
    public static Country getByAlpha3Code(String alpha3) {
        return null;
    }
}
