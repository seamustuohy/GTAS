package gov.gtas.services.security;

import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import gov.gtas.model.User;
import gov.gtas.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private UserRepository userRepository;

	@Autowired
	private UserServiceUtil userServiceUtil;
	
	private Pattern BCRYPT_PATTERN = Pattern
			.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

	@Override
	@Transactional
	public UserData create(UserData userData) {
		User userEntity = userServiceUtil.mapUserEntityFromUserData(userData);
		userEntity.setPassword((new BCryptPasswordEncoder()).encode(userEntity.getPassword()));
		User newUserEntity = userRepository.save(userEntity);
		UserData newUser = userServiceUtil.mapUserDataFromEntity(newUserEntity);
		return newUser;
	}

	@Override
	@Transactional
	public void delete(String id) {
		User userToDelete = userRepository.findOne(id);
		if (userToDelete != null)
			userRepository.delete(userToDelete);
	}

	@Override
	@Transactional
	public List<UserData> findAll() {
		Iterable<User> usersCollection = userRepository.findAll();
		List<UserData> users = userServiceUtil.getUserDataListFromEntityCollection(usersCollection);
		return users;
	}

	@Override
	@Transactional
	public UserData update(UserData data) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User entity = userRepository.findOne(data.getUserId());
		User mappedEnity = userServiceUtil.mapUserEntityFromUserData(data);
		if (entity != null) {
			entity.setFirstName(mappedEnity.getFirstName());
			entity.setLastName(mappedEnity.getLastName());
			if(!BCRYPT_PATTERN.matcher(mappedEnity.getPassword()).matches())
			{
				entity.setPassword(passwordEncoder.encode(mappedEnity.getPassword()));
			}else {
				entity.setPassword(mappedEnity.getPassword());
			}
			
			entity.setActive(mappedEnity.getActive());
			entity.setRoles(mappedEnity.getRoles());
			User savedEntity = userRepository.save(entity);
			UserData updatedUser = userServiceUtil.mapUserDataFromEntity(savedEntity);
			return updatedUser;
		}
		return null;
	}

	@Override
	@Transactional
	public UserData findById(String id) {
		User userEntity = userRepository.findOne(id);
		UserData userData = userServiceUtil.mapUserDataFromEntity(userEntity);
		return userData;

	}
}
