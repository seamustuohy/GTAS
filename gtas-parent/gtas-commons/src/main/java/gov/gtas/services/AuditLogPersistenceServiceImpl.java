package gov.gtas.services;

import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.enumtype.AuditActionType;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.AuditRecord;
import gov.gtas.model.User;
import gov.gtas.repository.AuditRecordRepository;
import gov.gtas.services.security.UserData;
import gov.gtas.services.security.UserService;
import gov.gtas.services.security.UserServiceUtil;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditLogPersistenceServiceImpl implements
		AuditLogPersistenceService {

	@Resource
	private AuditRecordRepository auditLogRepository;
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserServiceUtil userServiceUtil;

	
	@Override
	public AuditRecord create(AuditRecord aRec) {
		return auditLogRepository.save(aRec);
	}

	@Override
	public AuditRecord findById(Long id) {
		return auditLogRepository.findOne(id);
	}

	@Override
	public List<AuditRecord> findByDateRange(Date dateFrom, Date dateTo) {
		return auditLogRepository.findByTimestampRange(dateFrom, dateTo);
	}

	@Override
	public List<AuditRecord> findByDateFrom(Date dateFrom) {
		return auditLogRepository.findByTimestampFrom(dateFrom);
	}

	@Override
	public List<AuditRecord> findByUser(String userId) {
		User user = fetchUser(userId);
		return auditLogRepository.findByUser(user);
	}

	@Override
	public List<AuditRecord> findByActionType(AuditActionType actionType) {
		return auditLogRepository.findByActionType(actionType);
	}

	@Override
	public List<AuditRecord> findByUserAndActionType(AuditActionType actionType,
			String userId) {
		User user = fetchUser(userId);
		return auditLogRepository.findByUserAndActionType(user, actionType);
	}
	/**
	 * Fetches the user object and throws an unchecked exception if the user
	 * cannot be found.
	 * 
	 * @param userId
	 *            the ID of the user to fetch.
	 * @return the user fetched from the DB.
	 */
	private User fetchUser(final String userId) {
		UserData userData = userService.findById(userId);
		final User user = userServiceUtil.mapUserEntityFromUserData(userData);
		if (user.getUserId() == null) {
			ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
			throw errorHandler.createException(
					CommonErrorConstants.INVALID_USER_ID_ERROR_CODE, userId);
		}
		return user;
	}

}
