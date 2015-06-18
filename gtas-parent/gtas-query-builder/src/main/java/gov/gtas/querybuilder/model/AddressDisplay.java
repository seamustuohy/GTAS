package gov.gtas.querybuilder.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "address_ui_mapping")
public class AddressDisplay extends BaseDisplay implements IDisplay{

}
