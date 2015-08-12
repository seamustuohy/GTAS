package gov.gtas.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "gtas_roles")
public class Role implements Serializable{
    private static final long serialVersionUID = 1L;  
	public Role() { }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="role_id")
	private Integer roleId;
	
    @Column(name="role_description")
	private String roleDescription;
    
    @OneToMany(mappedBy = "userRole")   
	private List<User> userList;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

    public List<User> getUserList() {
        return userList;
    }
    
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
}
