package gov.gtas.model.watchlist.json.validation;

import static org.junit.Assert.*;

import java.util.List;

import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.constant.WatchlistConstants;
import gov.gtas.error.CommonValidationException;
import gov.gtas.model.watchlist.json.WatchlistSpec;
import gov.gtas.util.SampleDataGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.FieldError;

public class WatchlistValidationAdapterTest {
	private static final String WL_NAME1 = "Hello WL 1";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidateWatchlistSpec() {
		try{
		WatchlistSpec spec = SampleDataGenerator.newWlWith2Items(WL_NAME1);
        WatchlistValidationAdapter.validateWatchlistSpec(spec);
		} catch (Exception  ex){
			ex.printStackTrace();
			fail("Not expecting exception");
		}
	}
	@Test
	public void testMissingName() {
		try{
			WatchlistSpec spec = SampleDataGenerator.newWlWith2Items(WL_NAME1);
			spec.setName("");
	        WatchlistValidationAdapter.validateWatchlistSpec(spec);
			fail("Expecting exception");
		} catch (CommonValidationException  ex){
			assertEquals("JSON_INPUT_VALIDATION_ERROR", ex.getErrorCode());
			List<FieldError> fieldErrors = ex.getValidationErrors().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertEquals(WatchlistConstants.WL_NAME_FIELD, fieldErrors.get(0).getField());
		}
	}

}
