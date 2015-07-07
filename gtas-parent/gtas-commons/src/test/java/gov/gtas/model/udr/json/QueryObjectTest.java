package gov.gtas.model.udr.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.gtas.model.udr.EntityAttributeConstants;
import gov.gtas.model.udr.enumtype.EntityLookupEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.model.udr.json.util.UdrSpecificationBuilder;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QueryObjectTest {
    
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEmpty() {
		UdrSpecificationBuilder builder = new UdrSpecificationBuilder(null,
				QueryConditionEnum.OR);

		QueryObject qobj = builder.build().getDetails();
		List<List<QueryTerm>> flatList = qobj.createFlattenedList();
		assertNotNull(flatList);
		assertEquals(0,flatList.size());
	}

	@Test
	public void testOneTerm() {
		UdrSpecificationBuilder builder = new UdrSpecificationBuilder(null,
				QueryConditionEnum.AND);
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_CITIZENSHIP_COUNTRY, 
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL, new String[]{"Jones"});
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_DOB, 
				ValueTypesEnum.String, OperatorCodeEnum.LESS, new String[]{"1978-12-24"});
		QueryObject qobj = builder.build().getDetails();
		List<List<QueryTerm>> flatList = qobj.createFlattenedList();
		assertNotNull(flatList);
		assertEquals(1,flatList.size());
		List<QueryTerm> mintermList = flatList.get(0);
		assertEquals(2,mintermList.size());
	}
	@Test
	public void testAndOr() {
		UdrSpecificationBuilder builder = new UdrSpecificationBuilder(null,
				QueryConditionEnum.AND);
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_LAST_NAME, 
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL, new String[]{"Jones"});
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_DOB, 
				ValueTypesEnum.String, OperatorCodeEnum.LESS, new String[]{"1978-12-24"});
		builder.addNestedQueryObject(QueryConditionEnum.OR);
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_EMBARKATION_AIRPORT_NAME, 
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL, new String[]{"DBY"});
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_CITIZENSHIP_COUNTRY, 
				ValueTypesEnum.String, OperatorCodeEnum.NOT_EQUAL, new String[]{"USA"});
		
		QueryObject qobj = builder.build().getDetails();
		List<List<QueryTerm>> flatList = qobj.createFlattenedList();
		assertNotNull(flatList);
		assertEquals(2,flatList.size());
		List<QueryTerm> mintermList = flatList.get(0);
		assertEquals(3,mintermList.size());
		verifyTerm(mintermList.get(0), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_LAST_NAME, OperatorCodeEnum.EQUAL, new String[]{"Jones"} );
		verifyTerm(mintermList.get(1), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_DOB, OperatorCodeEnum.LESS, new String[]{"1978-12-24"} );
		verifyTerm(mintermList.get(2), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_EMBARKATION_AIRPORT_NAME, OperatorCodeEnum.EQUAL, new String[]{"DBY"} );
		
		mintermList = flatList.get(1);
		assertEquals(3,mintermList.size());
		verifyTerm(mintermList.get(0), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_LAST_NAME, OperatorCodeEnum.EQUAL, new String[]{"Jones"} );
		verifyTerm(mintermList.get(1), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_DOB, OperatorCodeEnum.LESS, new String[]{"1978-12-24"} );
		verifyTerm(mintermList.get(2), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_CITIZENSHIP_COUNTRY, OperatorCodeEnum.NOT_EQUAL, new String[]{"USA"} );
    }
	private void verifyTerm(QueryTerm trm, EntityLookupEnum entity, String attr, OperatorCodeEnum op, String[] val){
		assertEquals(entity.toString(), trm.getEntity());
		assertEquals(attr, trm.getField());
		assertEquals(op.toString(), trm.getOperator());
		assertEquals(val[0], trm.getValue());
		
	}

	@Test
	public void testFourLevel() {
		UdrSpecificationBuilder builder = new UdrSpecificationBuilder(null,
				QueryConditionEnum.AND);
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_LAST_NAME, 
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL, new String[]{"Jones"});
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_DOB, 
				ValueTypesEnum.String, OperatorCodeEnum.LESS, new String[]{"1978-12-24"});
		builder.addNestedQueryObject(QueryConditionEnum.OR);
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_EMBARKATION_AIRPORT_NAME, 
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL, new String[]{"DBY"});
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_CITIZENSHIP_COUNTRY, 
				ValueTypesEnum.String, OperatorCodeEnum.NOT_EQUAL, new String[]{"USA"});
		builder.addNestedQueryObject(QueryConditionEnum.AND);
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_MIDDLE_NAME, 
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL, new String[]{"Paul"});
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTR_FIRST_NAME, 
				ValueTypesEnum.String, OperatorCodeEnum.NOT_EQUAL, new String[]{"John"});
		builder.addNestedQueryObject(QueryConditionEnum.OR);
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.FLIGHT_ATTR_FLIGHT_NUMBER, 
				ValueTypesEnum.String, OperatorCodeEnum.EQUAL, new String[]{"12345"});
		builder.addTerm(EntityLookupEnum.Pax, EntityAttributeConstants.FLIGHT_ATTR_DESTINATION_NAME, 
				ValueTypesEnum.String, OperatorCodeEnum.NOT_EQUAL, new String[]{"Timbuktu"});

		QueryObject qobj = builder.build().getDetails();
		List<List<QueryTerm>> flatList = qobj.createFlattenedList();
		assertNotNull(flatList);
		assertEquals(4,flatList.size());
		List<QueryTerm> mintermList = flatList.get(0);
		assertEquals(3,mintermList.size());
		verifyTerm(mintermList.get(0), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_LAST_NAME, OperatorCodeEnum.EQUAL, new String[]{"Jones"} );
		verifyTerm(mintermList.get(1), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_DOB, OperatorCodeEnum.LESS, new String[]{"1978-12-24"} );
		verifyTerm(mintermList.get(2), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_EMBARKATION_AIRPORT_NAME, OperatorCodeEnum.EQUAL, new String[]{"DBY"} );
		
		mintermList = flatList.get(1);
		assertEquals(3,mintermList.size());
		verifyTerm(mintermList.get(0), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_LAST_NAME, OperatorCodeEnum.EQUAL, new String[]{"Jones"} );
		verifyTerm(mintermList.get(1), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_DOB, OperatorCodeEnum.LESS, new String[]{"1978-12-24"} );
		verifyTerm(mintermList.get(2), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_CITIZENSHIP_COUNTRY, OperatorCodeEnum.NOT_EQUAL, new String[]{"USA"} );

		mintermList = flatList.get(2);
		assertEquals(5,mintermList.size());
		verifyTerm(mintermList.get(0), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_LAST_NAME, OperatorCodeEnum.EQUAL, new String[]{"Jones"} );
		verifyTerm(mintermList.get(1), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_DOB, OperatorCodeEnum.LESS, new String[]{"1978-12-24"} );
		verifyTerm(mintermList.get(2), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_MIDDLE_NAME, OperatorCodeEnum.EQUAL, new String[]{"Paul"} );
		verifyTerm(mintermList.get(3), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTR_FIRST_NAME, OperatorCodeEnum.NOT_EQUAL, new String[]{"John"} );
		verifyTerm(mintermList.get(4), EntityLookupEnum.Pax, EntityAttributeConstants.FLIGHT_ATTR_FLIGHT_NUMBER, OperatorCodeEnum.EQUAL, new String[]{"12345"} );

		mintermList = flatList.get(3);
		assertEquals(5,mintermList.size());
		verifyTerm(mintermList.get(0), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_LAST_NAME, OperatorCodeEnum.EQUAL, new String[]{"Jones"} );
		verifyTerm(mintermList.get(1), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_DOB, OperatorCodeEnum.LESS, new String[]{"1978-12-24"} );
		verifyTerm(mintermList.get(2), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTTR_MIDDLE_NAME, OperatorCodeEnum.EQUAL, new String[]{"Paul"} );
		verifyTerm(mintermList.get(3), EntityLookupEnum.Pax, EntityAttributeConstants.PAX_ATTR_FIRST_NAME, OperatorCodeEnum.NOT_EQUAL, new String[]{"John"} );
		verifyTerm(mintermList.get(4), EntityLookupEnum.Pax, EntityAttributeConstants.FLIGHT_ATTR_DESTINATION_NAME, OperatorCodeEnum.NOT_EQUAL, new String[]{"Timbuktu"} );
				
	}
}
