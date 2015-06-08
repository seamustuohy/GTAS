package gov.cbp.taspd.gtas.services;

import java.util.List;

import gov.cbp.taspd.gtas.model.User;

public interface UserService {
	public User create(User user);
    public User delete(String id);
    public List<User> findAll();
    public User update(User user) ;
    public User findById(String id);


}
