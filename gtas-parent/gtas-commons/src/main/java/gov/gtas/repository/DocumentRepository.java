package gov.gtas.repository;

import gov.gtas.model.Document;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DocumentRepository extends CrudRepository<Document, Long>{
    @Query("SELECT d FROM Document d WHERE traveler_id = :id")
    public List<Document> getTravelerDocuments(@Param("id") Long id);
}
