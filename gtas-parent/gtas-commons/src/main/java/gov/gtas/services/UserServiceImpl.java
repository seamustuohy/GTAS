package gov.gtas.services;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import gov.gtas.model.User;
import gov.gtas.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Resource
	private UserRepository userRespository;

	@Override
	@Transactional
	public User create(User user) {
		
		return userRespository.save(user);
	}

	@Override
	@Transactional
	public User delete(String id) {
		User UserToDelete = this.findById(id);
		if(UserToDelete != null)
			userRespository.delete(UserToDelete);
		return UserToDelete;
	}

	@Override
	@Transactional
	public List<User> findAll() {
		return (List<User>)userRespository.findAll();
	}

	@Override
	@Transactional
	public User update(User user) {
		User updateUser =this.findById(user.getUserId());
		if(updateUser != null){
			updateUser.setFirstName(user.getFirstName());
			updateUser.setLastName(user.getLastName());
			updateUser.setPassword(user.getPassword());
			updateUser.setActive(user.getActive());
			updateUser.setRoles(user.getRoles());
			return userRespository.save(updateUser);			
		}

		return updateUser;
	}

	@Override
	@Transactional
	public User findById(String id) {
		User user = userRespository.findOne(id);
		return user;
	}
}
