package gov.gtas.repository.udr;

import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.YesNoEnum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UdrRuleRepository extends CrudRepository<UdrRule, Long>, JpaSpecificationExecutor<UdrRule> {
   List<UdrRule> findByDeleted(YesNoEnum deleted);
}
