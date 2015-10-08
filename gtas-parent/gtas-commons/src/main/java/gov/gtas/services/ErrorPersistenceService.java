package gov.gtas.services;

import gov.gtas.error.ErrorDetails;

import java.util.List;

public interface ErrorPersistenceService {
	public ErrorDetails create(ErrorDetails error);
    public ErrorDetails findById(Long id);

    public List<ErrorDetails> findByDateRange(String jsonDateFrom, String jsonDateTo);
    public List<ErrorDetails> findByDateFrom(String jsonDateFrom);

}
