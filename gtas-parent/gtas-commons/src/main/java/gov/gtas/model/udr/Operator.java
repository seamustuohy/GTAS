package gov.gtas.model.udr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Operator
 */
@Entity
@Table(name = "OPCODE_LOOKUP")
public class Operator implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1969961710671243943L;
	private String opCode;
	private String description;

	public Operator() {
	}

	public Operator(String opCode) {
		this.opCode = opCode;
	}

	public Operator(String opCode, String description) {
		this.opCode = opCode;
		this.description = description;
	}

	@Id
	@Column(name = "OP_CODE", unique = true, nullable = false, length = 16)
	public String getOpCode() {
		return this.opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	@Column(name = "DESCRIPTION", length = 256)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
