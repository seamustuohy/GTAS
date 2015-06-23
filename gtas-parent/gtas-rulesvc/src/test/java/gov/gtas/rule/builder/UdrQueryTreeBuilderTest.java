package gov.gtas.rule.builder;

import static org.junit.Assert.*;

import java.util.List;

import gov.gtas.error.RuleServiceException;
import gov.gtas.model.udr.OperatorCodeEnum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UdrQueryTreeBuilderTest {
    private UdrQueryTreeBuilder testTarget;
    
	@Before
	public void setUp() throws Exception {
		testTarget = new UdrQueryTreeBuilder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=RuleServiceException.class)
	public void testError1() {
		testTarget.getFlattenedQueryTree();
	}

	@Test(expected=RuleServiceException.class)
	public void testError2() {
		testTarget.beginTree(CondOpEnum.AND);
		testTarget.addLeaf("A", "B", OperatorCodeEnum.EQUAL, 5);
		testTarget.addLeaf("A", "C", OperatorCodeEnum.EQUAL, 7);
		testTarget.getFlattenedQueryTree();
	}
	@Test
	public void testSimple1() {
		testTarget.beginTree(CondOpEnum.AND);
		testTarget.addLeaf("A", "B", OperatorCodeEnum.EQUAL, 5);
		testTarget.addLeaf("A", "C", OperatorCodeEnum.NOT_EQUAL, 7);
		testTarget.endTree();
		List<List<Term>> flatList = testTarget.getFlattenedQueryTree();
		assertNotNull("Expected a non null flattened minterm list", flatList);
		assertEquals("Expected a list ofone minterm", 1,flatList.size());
		assertEquals("Expected a minterm with two terms", 2,flatList.get(0).size());
		Term term = flatList.get(0).get(0);
		assertEquals("A", term.getEntity());
		assertEquals("B",term.getAttribute());
		assertEquals(OperatorCodeEnum.EQUAL, term.getOperator());
		assertEquals(5, term.getValue());

		term = flatList.get(0).get(1);
		assertEquals("A", term.getEntity());
		assertEquals("C",term.getAttribute());
		assertEquals(OperatorCodeEnum.NOT_EQUAL, term.getOperator());
		assertEquals(7, term.getValue());
    }

	@Test
	public void testTwoLevel() {
		testTarget.beginTree(CondOpEnum.AND);
		testTarget.addLeaf("A", "B", OperatorCodeEnum.EQUAL, 5);
		testTarget.addLeaf("A", "C", OperatorCodeEnum.NOT_EQUAL, 7);
		testTarget.beginTree(CondOpEnum.OR);
		testTarget.addLeaf("X", "B", OperatorCodeEnum.EQUAL, 5);
		testTarget.addLeaf("X", "C", OperatorCodeEnum.NOT_EQUAL, 7);
		testTarget.endTree();
		testTarget.endTree();
		List<List<Term>> flatList = testTarget.getFlattenedQueryTree();
		assertNotNull("Expected a non null flattened minterm list", flatList);
		assertEquals("Expected a list of two minterms", 2,flatList.size());
		assertEquals("Expected a minterm with three terms", 3,flatList.get(0).size());
		Term term = flatList.get(0).get(0);
		assertEquals("A", term.getEntity());
		assertEquals("B",term.getAttribute());
		assertEquals(OperatorCodeEnum.EQUAL, term.getOperator());
		assertEquals(5, term.getValue());

		term = flatList.get(0).get(1);
		assertEquals("A", term.getEntity());
		assertEquals("C",term.getAttribute());
		assertEquals(OperatorCodeEnum.NOT_EQUAL, term.getOperator());
		assertEquals(7, term.getValue());
		
		term = flatList.get(0).get(2);
		assertEquals("X", term.getEntity());
		assertEquals("B",term.getAttribute());
		assertEquals(OperatorCodeEnum.EQUAL, term.getOperator());
		assertEquals(5, term.getValue());
       
		//the second minterm
		term = flatList.get(1).get(0);
		assertEquals("A", term.getEntity());
		assertEquals("B",term.getAttribute());
		assertEquals(OperatorCodeEnum.EQUAL, term.getOperator());
		assertEquals(5, term.getValue());

		term = flatList.get(1).get(1);
		assertEquals("A", term.getEntity());
		assertEquals("C",term.getAttribute());
		assertEquals(OperatorCodeEnum.NOT_EQUAL, term.getOperator());
		assertEquals(7, term.getValue());
		
		term = flatList.get(1).get(2);
		assertEquals("X", term.getEntity());
		assertEquals("C",term.getAttribute());
		assertEquals(OperatorCodeEnum.NOT_EQUAL, term.getOperator());
		assertEquals(7, term.getValue());
		
	
	}
}
