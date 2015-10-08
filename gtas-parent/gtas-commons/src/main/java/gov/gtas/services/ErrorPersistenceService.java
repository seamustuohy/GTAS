package gov.gtas.services;

import gov.gtas.error.ErrorDetailInfo;

import java.util.List;

public interface ErrorPersistenceService {
	public ErrorDetailInfo create(ErrorDetailInfo error);
    public ErrorDetailInfo findById(Long id);

    public List<ErrorDetailInfo> findByDateRange(String jsonDateFrom, String jsonDateTo);
    public List<ErrorDetailInfo> findByDateFrom(String jsonDateFrom);

}
