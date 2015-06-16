package gov.cbp.taspd.gtas.querybuilder.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "document_ui_mapping")
public class DocumentDisplay extends BaseDisplay implements IDisplay {

}
