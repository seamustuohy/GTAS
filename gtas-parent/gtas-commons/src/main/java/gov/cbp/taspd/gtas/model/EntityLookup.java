package gov.cbp.taspd.gtas.model;

// Generated Jun 15, 2015 7:40:37 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EntityLookup generated by hbm2java
 */
@Entity
@Table(name = "entity_lookup", catalog = "gtas")
public class EntityLookup implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5831864980342714684L;
	private String entityName;
	private String description;

	public EntityLookup() {
	}

	public EntityLookup(String entityName) {
		this.entityName = entityName;
	}

	public EntityLookup(String entityName, String description) {
		this.entityName = entityName;
		this.description = description;
	}

	@Id
	@Column(name = "ENTITY_NAME", unique = true, nullable = false, length = 64)
	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@Column(name = "DESCRIPTION", length = 256)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
