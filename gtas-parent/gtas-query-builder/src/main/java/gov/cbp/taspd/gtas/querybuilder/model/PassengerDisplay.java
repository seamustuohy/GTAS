package gov.cbp.taspd.gtas.querybuilder.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "passenger_ui_mapping")
public class PassengerDisplay extends BaseDisplay implements IDisplay {

}
