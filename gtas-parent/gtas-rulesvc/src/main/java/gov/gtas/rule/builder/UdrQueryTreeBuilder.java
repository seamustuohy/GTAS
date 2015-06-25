package gov.gtas.rule.builder;

import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.error.RuleServiceErrorHandler;
import gov.gtas.model.udr.OperatorCodeEnum;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Build a Query tree with AND and OR nodes. <br>
 * After the tree is constructed, it can be flattened to a two level
 * tree with OR at the root and AND expressions as children.
 * 
 * @author GTAS3 (AB)
 *
 */
public class UdrQueryTreeBuilder {
	/*
	 * The logger for the UdrQueryTreeBuilder
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(UdrQueryTreeBuilder.class);

	private QueryTreeNode root;
	private QueryTreeNode currentNode;
	private int level = 0;
	
     public void beginTree(CondOpEnum condOp){
    	 if(root == null){
    		 //new tree
    		 root = new QueryTreeNode(null, condOp);
    		 currentNode = root;
    	 }else{
    		 QueryTreeNode newChild = new QueryTreeNode(currentNode, condOp);
    		 currentNode.addChild(newChild);
    		 currentNode = newChild;
    	 }
    	 ++level;
     }
     public void addLeaf(String entity, String attribute, OperatorCodeEnum op, Object value){
    	 if(currentNode != null){
    		 Term term = new Term(currentNode, entity, attribute, op, value);
    		 currentNode.addChild(term);
    	 }else{
    		 //TODO Error
    		 logger.error("UdrQueryTreeBuilder.addLeaf() - attempted to add leaf in invalid tree state!");
    	 }
     }
     public void endTree(){
    	 this.currentNode = this.currentNode.getParent();
    	 --level;
     }
     public List<List<Term>> getFlattenedQueryTree(){
    	 //check that the tree has been fully constructed
    	 if(root != null && currentNode == null && level ==0){
    		 return root.getFlattenedList();
    	 } else {
    		 RuleServiceErrorHandler errorHandler = new RuleServiceErrorHandler();
    		 throw errorHandler.createException(RuleServiceConstants.INCOMPLETE_TREE_ERROR_CODE, level);
    	 }
    	 
     }
}
