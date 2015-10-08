package gov.gtas.services;

import gov.gtas.error.BasicErrorDetailInfo;
import gov.gtas.error.ErrorDetailInfo;
import gov.gtas.model.ErrorRecord;
import gov.gtas.repository.ErrorRecordRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
@Service
public class ErrorPersistenceServiceImpl implements ErrorPersistenceService{
    @Resource
    private ErrorRecordRepository errorRecordRepository;
    
	/* (non-Javadoc)
	 * @see gov.gtas.services.ErrorPersistenceService#create(gov.gtas.error.ErrorDetails)
	 */
	@Override
	public ErrorDetailInfo create(ErrorDetailInfo error) {
		ErrorRecord err =  errorRecordRepository.save(new ErrorRecord(error));
		return new BasicErrorDetailInfo(err.getId(), err.getCode(), err.getDescription(), err.fetchErrorDetails());
	}

	/* (non-Javadoc)
	 * @see gov.gtas.services.ErrorPersistenceService#findById(java.lang.Long)
	 */
	@Override
	public ErrorDetailInfo findById(Long id) {
		ErrorRecord err =  errorRecordRepository.findOne(id);
		return new BasicErrorDetailInfo(err.getId(), err.getCode(), err.getDescription(), err.fetchErrorDetails());
	}

	/* (non-Javadoc)
	 * @see gov.gtas.services.ErrorPersistenceService#findByDateRange(java.lang.String, java.lang.String)
	 */
	@Override
	public List<ErrorDetailInfo> findByDateRange(Date fromDate,
			Date toDate) {
		return convert(errorRecordRepository.findByTimestampRange(fromDate, toDate));
	}

	/* (non-Javadoc)
	 * @see gov.gtas.services.ErrorPersistenceService#findByDateFrom(java.lang.String)
	 */
	@Override
	public List<ErrorDetailInfo> findByDateFrom(Date fromDate) {
		return convert(errorRecordRepository.findByTimestampFrom(fromDate));
	}

	/* (non-Javadoc)
	 * @see gov.gtas.services.ErrorPersistenceService#findByCode(java.lang.String)
	 */
	@Override
	public List<ErrorDetailInfo> findByCode(String code) {
		return convert(errorRecordRepository.findByCode(code));
	}

	private List<ErrorDetailInfo> convert(List<ErrorRecord> lst){
		List<ErrorDetailInfo> ret = null;
		if(!CollectionUtils.isEmpty(lst)){
			ret = lst.stream().map(
					               (ErrorRecord e)->
					                    new BasicErrorDetailInfo(e.getId(), e.getCode(), e.getDescription(), e.fetchErrorDetails()))
					          .collect(Collectors.toList());
		}
		return ret;
		
	}
}
