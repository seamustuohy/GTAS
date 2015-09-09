package gov.gtas.services;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import gov.gtas.model.Role;
import gov.gtas.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private RoleRepository roleRepository;

	@Override
	@Transactional
	public List<Role> findAll() {
		return (List<Role>) roleRepository.findAll();
	}
}
