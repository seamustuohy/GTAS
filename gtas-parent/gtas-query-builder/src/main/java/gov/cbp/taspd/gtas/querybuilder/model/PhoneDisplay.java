package gov.cbp.taspd.gtas.querybuilder.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "phone_ui_mapping")
public class PhoneDisplay extends BaseDisplay implements IDisplay {

}
