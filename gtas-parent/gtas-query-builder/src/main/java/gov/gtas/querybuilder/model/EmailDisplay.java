package gov.gtas.querybuilder.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "email_ui_mapping")
public class EmailDisplay extends BaseDisplay implements IDisplay {

}
