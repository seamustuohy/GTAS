package gov.gtas.repository;

import java.util.List;

import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.YesNoEnum;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface RuleRepository extends CrudRepository<Rule, Long>, JpaSpecificationExecutor<Rule> {
   List<Rule> findByDeleted(YesNoEnum deleted);
}
