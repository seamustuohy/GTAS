package gov.gtas.udrbuilder.repository;

import gov.gtas.model.udr.CondValue;
import gov.gtas.model.udr.CondValuePk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CondValueRepository extends
		JpaRepository<CondValue, CondValuePk> {

}
