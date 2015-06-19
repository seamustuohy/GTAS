package gov.gtas.udrbuilder.repository;

import gov.gtas.model.udr.CondValue;
import gov.gtas.model.udr.CondValuePk;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CondValueRepository extends
		JpaRepository<CondValue, CondValuePk> {

}
