package gov.gtas.services;

import gov.gtas.error.BasicErrorDetails;
import gov.gtas.error.ErrorDetails;
import gov.gtas.model.ErrorRecord;
import gov.gtas.repository.ErrorRecordRepository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
@Service
public class ErrorPersistenceServiceImpl implements ErrorPersistenceService{
    @Resource
    private ErrorRecordRepository errorRecordRepository;
    
	/* (non-Javadoc)
	 * @see gov.gtas.services.ErrorPersistenceService#create(gov.gtas.error.ErrorDetails)
	 */
	@Override
	public ErrorDetails create(ErrorDetails error) {
		ErrorRecord err =  errorRecordRepository.save(new ErrorRecord(error));
		return new BasicErrorDetails(err.getId(), err.getCode(), err.getDescription(), err.fetchErrorDetails());
	}

	/* (non-Javadoc)
	 * @see gov.gtas.services.ErrorPersistenceService#findById(java.lang.Long)
	 */
	@Override
	public ErrorDetails findById(Long id) {
		ErrorRecord err =  errorRecordRepository.findOne(id);
		return new BasicErrorDetails(err.getId(), err.getCode(), err.getDescription(), err.fetchErrorDetails());
	}

	/* (non-Javadoc)
	 * @see gov.gtas.services.ErrorPersistenceService#findByDateRange(java.lang.String, java.lang.String)
	 */
	@Override
	public List<ErrorDetails> findByDateRange(String jsonDateFrom,
			String jsonDateTo) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gov.gtas.services.ErrorPersistenceService#findByDateFrom(java.lang.String)
	 */
	@Override
	public List<ErrorDetails> findByDateFrom(String jsonDateFrom) {
		// TODO Auto-generated method stub
		return null;
	}

}
