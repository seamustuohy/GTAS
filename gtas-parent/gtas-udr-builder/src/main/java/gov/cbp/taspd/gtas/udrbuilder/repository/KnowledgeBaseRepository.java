package gov.cbp.taspd.gtas.udrbuilder.repository;

import gov.gtas.model.udr.KnowledgeBase;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeBaseRepository extends
		JpaRepository<KnowledgeBase, Long> {

}
