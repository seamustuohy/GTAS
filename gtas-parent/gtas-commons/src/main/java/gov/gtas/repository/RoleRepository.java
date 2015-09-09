package gov.gtas.repository;

import org.springframework.data.repository.CrudRepository;

import gov.gtas.model.Role;

public interface RoleRepository extends CrudRepository<Role, String> {

}
