package gov.gtas.model;

import gov.gtas.constant.DomainModelConstants;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "gtas_users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;  
    public User() { }

    @Id
    @Column(name="user_id", length=DomainModelConstants.GTAS_USERID_COLUMN_SIZE)
	private String userId;

    @Column(name="password", nullable = false)
    private String password;
    
    @Column(name="first_name")
	private String firstName;

    @Column(name="last_name")
	private String lastName;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name ="role_id")
	private Role userRole;
    
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name ="userId")    
	private List<Authorities> authoritiesList;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Role getUserRole() {
		return userRole;
	}
	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}
	
	public Collection<Authorities> getAuthorities(){
		return authoritiesList;
	}

	public void setAuthorities(List<Authorities> authList){
		this.authoritiesList = authList;
	}
}
