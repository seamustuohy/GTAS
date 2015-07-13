package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.error.BasicErrorHandler;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.CommonServiceException;
import gov.gtas.error.ErrorDetails;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.udr.json.JsonUdrListElement;
import gov.gtas.model.udr.json.MetaData;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.model.udr.json.error.GtasJsonError;
import gov.gtas.model.udr.json.util.UdrSpecificationBuilder;
import gov.gtas.svc.UdrService;
import gov.gtas.util.DateCalendarUtils;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST service end-point controller for creating and managing user Defined
 * Rules (UDR) for targeting.
 * 
 * @author GTAS3 (AB)
 *
 */
@RestController
@RequestMapping(Constants.UDR_ROOT)
public class UdrManagementController {
	/*
	 * The logger for the UdrManagementController
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(UdrManagementController.class);

	@Autowired
	private UdrService udrService;

	@RequestMapping(value = Constants.UDR_GET, method = RequestMethod.GET)
	public @ResponseBody UdrSpecification getUDR(@PathVariable String userId,
			@PathVariable String title) {
		System.out.println("******** user =" + userId + ", title=" + title);
		UdrSpecification resp = udrService.fetchUdr(userId, title);
		return resp;
	}

	@RequestMapping(value = Constants.UDR_GET_BY_ID, method = RequestMethod.GET)
	public @ResponseBody UdrSpecification getUDRById(@PathVariable Long id) {
		System.out.println("******** Received GET UDR request for id=" + id);
		UdrSpecification resp = udrService.fetchUdr(id);
		return resp;
	}

	@RequestMapping(value = Constants.UDR_GETALL, method = RequestMethod.GET)
	public @ResponseBody List<JsonUdrListElement> getUDRList(
			@PathVariable String userId) {
		System.out.println("******** user =" + userId);
		List<JsonUdrListElement> resp = udrService.fetchUdrSummaryList(userId);
		return resp;
	}

	@RequestMapping(value = Constants.UDR_POST, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JsonServiceResponse createUDR(
			@PathVariable String userId, @RequestBody UdrSpecification inputSpec) {

		logger.info("******** Received UDR Create request by user =" + userId);
		if (inputSpec == null) {
			throw new CommonServiceException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					String.format(
							CommonErrorConstants.NULL_ARGUMENT_ERROR_MESSAGE,
							"Create Query For Rule", "inputSpec"));
		}
		/*
		 * The Jackson JSON parser assumes that the time zone is GMT if no
		 * offset is explicitly indicated. Thus "2015-07-10" is interpreted as
		 * "2015-07-10T00:00:00" GMT or "2015-07-09T20:00:00" EDT, i.e., the
		 * previous day. The following 3 lines of code reverses this interpretation.
		 */
		MetaData meta = inputSpec.getSummary();
		meta.setStartDate(fixMetaDataDates(meta.getStartDate()));
		meta.setEndDate(fixMetaDataDates(meta.getEndDate()));
		
		JsonServiceResponse resp = udrService.createUdr(userId, inputSpec);

		return resp;
	}

	/**
	 * Subtracts the offset to reverse the interpretation at GMT time.
	 * @param inputUdrdate the date to "fix"
	 * @return the fixed date.
	 */
	private Date fixMetaDataDates(Date inputUdrdate) {
		if (inputUdrdate != null) {
			long offset = DateCalendarUtils
					.calculateOffsetFromGMT(inputUdrdate);
			return new Date(inputUdrdate.getTime() - offset);
		} else {
			return null;
		}

	}

	@RequestMapping(value = Constants.UDR_PUT, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JsonServiceResponse updateUDR(
			@PathVariable String userId, @RequestBody UdrSpecification inputSpec) {
		logger.info("******** Received UDR Update request by user =" + userId);
		
		/*
		 * The Jackson JSON parser assumes that the time zone is GMT if no
		 * offset is explicitly indicated. Thus "2015-07-10" is interpreted as
		 * "2015-07-10T00:00:00" GMT or "2015-07-09T20:00:00" EDT, i.e., the
		 * previous day. The following 3 lines of code reverses this interpretation.
		 */
		MetaData meta = inputSpec.getSummary();
		meta.setStartDate(fixMetaDataDates(meta.getStartDate()));
		meta.setEndDate(fixMetaDataDates(meta.getEndDate()));
				
		JsonServiceResponse resp = udrService.updateUdr(userId, inputSpec);

		return resp;
	}

	@RequestMapping(value = Constants.UDR_DELETE, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JsonServiceResponse deleteUDR(
			@PathVariable String userId, @PathVariable Long id) {
		logger.info("******** Received UDR Delete request by user =" + userId
				+ " for " + id);
		JsonServiceResponse resp = udrService.deleteUdr(userId, id);

		return resp;
	}

	@RequestMapping(value = Constants.UDR_TEST, method = RequestMethod.GET)
	public @ResponseBody UdrSpecification getUDR() {
		UdrSpecification resp = UdrSpecificationBuilder.createSampleSpec();
		return resp;
	}

	@ExceptionHandler(CommonServiceException.class)
	public @ResponseBody GtasJsonError handleError(CommonServiceException ex) {
		ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
		ErrorDetails err = errorHandler.processError(ex);
		return new GtasJsonError(err.getFatalErrorCode(),
				err.getFatalErrorMessage());
		// return new GtasJsonError(ex.getErrorCode(), ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody GtasJsonError handleError(Exception ex) {
		ex.printStackTrace();
		GtasJsonError ret = handleSpecialError(ex);
		if (ret == null) {
			ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
			ErrorDetails err = errorHandler.processError(ex);
			ret = new GtasJsonError(err.getFatalErrorCode(),
					err.getFatalErrorMessage());
		}
		return ret;
	}

	private GtasJsonError handleSpecialError(Exception ex) {
		GtasJsonError ret = null;
		if (ex instanceof HttpMessageNotReadableException) {
			ret = new GtasJsonError("MALFORMED_JSON_INPUT",
					"Input JSON is malformed:" + ex.getMessage());
		} else if (ex instanceof JpaSystemException) {
			ret = new GtasJsonError("DUPLICATE_UDR_TITLE",
					"This author has already created a UDR with this title:"
							+ ex.getMessage());
		}
		return ret;
	}
}
