package gov.gtas.repository.udr;

import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.enumtype.YesNoEnum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UdrRuleRepository extends CrudRepository<UdrRule, Long>, JpaSpecificationExecutor<UdrRule> {
    public List<UdrRule> findByDeleted(YesNoEnum deleted);
    
	@Query("SELECT udr FROM UdrRule udr WHERE udr.deleted = 'N' and udr.author.userId = :authorUserId")
    public List<UdrRule> getUdrRuleByAuthor(@Param("authorUserId") String authorUserId);
    
	@Query("SELECT udr FROM UdrRule udr WHERE udr.metaData.title = :title and udr.author.userId = :authorUserId")
	public UdrRule getUdrRuleByTitleAndAuthor(@Param("title") String title, @Param("authorUserId") String authorUserId);
}
