package gov.gtas.udrbuilder.repository;

import gov.gtas.model.udr.KnowledgeBase;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeBaseRepository extends
		JpaRepository<KnowledgeBase, Long> {

	void delete(KnowledgeBase deleted);

	List<KnowledgeBase> findAll();

	KnowledgeBase findOne(Long id);

	@SuppressWarnings("unchecked")
	KnowledgeBase save(KnowledgeBase persisted);

}
