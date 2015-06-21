package gov.gtas.udrbuilder.repository;

import gov.gtas.model.udr.RuleCond;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleCondRepository extends JpaRepository<RuleCond, Long> {

}
