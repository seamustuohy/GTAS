package gov.cbp.taspd.gtas.model;

import javax.persistence.Entity;


@Entity
public class Carrier extends BaseEntity {
    public Carrier() { }
    
    private String name;
    private String iata;
    public String getName() {
        return name;
    }
    public String getIata() {
        return iata;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((iata == null) ? 0 : iata.hashCode());
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
        Carrier other = (Carrier) obj;
        if (iata == null) {
            if (other.iata != null)
                return false;
        } else if (!iata.equals(other.iata))
            return false;
        return true;
    }    
}
