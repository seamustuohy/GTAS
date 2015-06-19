package gov.gtas.services;

import java.util.List;

import gov.gtas.model.User;

public interface UserService {
	public User create(User user);
    public User delete(String id);
    public List<User> findAll();
    public User update(User user) ;
    public User findById(String id);


}
