package gov.gtas.repository.udr;

import gov.gtas.enumtype.YesNoEnum;
import gov.gtas.model.udr.KnowledgeBase;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.UdrRule;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
/**
 * Rule Repository with custom queries.
 * @author GTAS3 (AB)
 *
 */
public interface UdrRuleRepository extends CrudRepository<UdrRule, Long>, JpaSpecificationExecutor<UdrRule> {
    public List<UdrRule> findByDeleted(YesNoEnum deleted);
    
	@Query("SELECT udr FROM UdrRule udr WHERE udr.deleted = 'N' and udr.author.userId = :authorUserId")
    public List<UdrRule> getUdrRuleByAuthor(@Param("authorUserId") String authorUserId);
    
	@Query("SELECT udr FROM UdrRule udr WHERE udr.metaData.title = :title and udr.author.userId = :authorUserId")
	public UdrRule getUdrRuleByTitleAndAuthor(@Param("title") String title, @Param("authorUserId") String authorUserId);

	@Query("SELECT kb FROM KnowledgeBase kb WHERE kb.kbName = :name")
	public KnowledgeBase getKnowledgeBaseByName(@Param("name") String name);

	@Query("SELECT rl FROM Rule rl WHERE rl.knowledgeBase.id = :kbId")
	public List<Rule> getRuleByKbId(@Param("kbId") Long kbId);

	@Query("SELECT udr FROM UdrRule udr WHERE  udr.deleted = 'N' and udr.metaData.startDt <= :targetDate and (udr.metaData.endDt is null or udr.metaData.endDt >= :targetDate)")
	public List<UdrRule>findValidUdrRuleByDate(@Param("targetDate") Date targetDate);
}
