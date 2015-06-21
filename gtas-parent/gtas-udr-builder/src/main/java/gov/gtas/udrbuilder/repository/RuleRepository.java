package gov.gtas.udrbuilder.repository;

import gov.gtas.model.udr.Rule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

}
