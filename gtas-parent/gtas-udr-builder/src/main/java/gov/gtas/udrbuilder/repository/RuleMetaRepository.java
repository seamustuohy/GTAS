package gov.gtas.udrbuilder.repository;

import gov.gtas.model.udr.RuleMeta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleMetaRepository extends JpaRepository<RuleMeta, Long> {

}
