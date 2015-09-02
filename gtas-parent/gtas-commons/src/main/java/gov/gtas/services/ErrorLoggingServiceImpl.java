package gov.gtas.services;

import gov.gtas.model.InvalidObjectInfo;
import gov.gtas.repository.ErrorLoggingRepository;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

public class ErrorLoggingServiceImpl implements ErrorLoggingService {

	@Resource
	ErrorLoggingRepository dao;
	
	@Override
	@Transactional
	public InvalidObjectInfo create(InvalidObjectInfo invalidObjectInfo) {
		return dao.save(invalidObjectInfo);
	}

	@Override
	@Transactional
	public InvalidObjectInfo delete(Long id) {
		InvalidObjectInfo error = this.findById(id);
		dao.delete(error);
		return error;
	}

	@Override
	@Transactional
	public List<InvalidObjectInfo> findAll() {
		return (List<InvalidObjectInfo>) dao.findAll();
	}

	@Override
	@Transactional
	public InvalidObjectInfo update(InvalidObjectInfo port) {
		return null;
	}

	@Override
	@Transactional
	public InvalidObjectInfo findById(Long id) {
		return dao.findOne(id);
	}
}
