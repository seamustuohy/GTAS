package gov.gtas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "authorities")
public class Authorities implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private Integer id;
	private Role userRole;
	
	
	
	@Id
	@Column(name="id")
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	@Column(name="username")
	public String getUserId() {
		return userId;
	}
	
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name ="role_id")

	public Role getUserRole() {
		return userRole;
	}
	
	
	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}

	
}
