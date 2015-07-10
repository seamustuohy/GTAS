package gov.gtas.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "gtas_users")
public class User implements Serializable {


	private static final long serialVersionUID = 1L;
	private String userId;
	private String password;
	private String firstName;
	private String lastName;
	private Role userRole;
	private List<Authorities> authoritiesList;

	@Id
	@Column(name="user_id")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name="password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="first_name")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name="last_name")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name ="role_id")
	public Role getUserRole() {
		return userRole;
	}
	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name ="userId")
	public Collection<Authorities> getAuthorities(){
		return authoritiesList;
	}

	public void setAuthorities(List<Authorities> authList){
		this.authoritiesList = authList;
	}

}
