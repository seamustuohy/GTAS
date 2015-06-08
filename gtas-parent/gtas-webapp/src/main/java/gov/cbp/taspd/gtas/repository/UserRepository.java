package gov.cbp.taspd.gtas.repository;

import gov.cbp.taspd.gtas.model.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

}
