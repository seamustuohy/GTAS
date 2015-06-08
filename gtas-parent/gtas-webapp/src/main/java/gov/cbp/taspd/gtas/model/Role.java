package gov.cbp.taspd.gtas.model;

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
@Table(name = "gtas_roles", schema="gtas")
public class Role implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	private Integer roleId;
	private String roleDescription;
	private List<User> userList;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="role_id")
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Column(name="role_description")
	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	
    @OneToMany(mappedBy = "userRole")
    public List<User> getUserList() {
        return userList;
    }
    
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

}
