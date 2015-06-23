package gov.gtas.rule.builder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/*
 * A query condition tree node with AND and OR nodes according to the grammar:<br>
 * EXPR ->  COND_OP EXPR_LIST <br>
 * COND_OP -> "AND" | "OR" <br>
 * EXPR_LIST -> TERM EXPR_LIST | EXPR EXPR_LIST | TERM <br>
 * TERM -> ENTITY ATTRIBUTE OPERATOR VALUE <br>
 *
 *This class represents the EXPR item in the above grammar.
 *
 * @author GTAS3 (AB)
 *
 */
public class QueryTreeNode {
   private QueryTreeNode parent;
   private CondOpEnum condOp;
   private List<QueryTreeNode> children;
   
   /**
    * Constructor.
    * @param condOp
    */
   public QueryTreeNode(QueryTreeNode parent, CondOpEnum condOp){
	   this.parent = parent;
	   this.condOp = condOp;
	   children = new LinkedList<QueryTreeNode>();
   }
	/**
	 * @return the parent
	 */
	public QueryTreeNode getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(QueryTreeNode parent) {
		this.parent = parent;
	}
	/**
	 * @return the children
	 */
	public List<QueryTreeNode> getChildren() {
		return Collections.unmodifiableList(children);
	}
	
	/**
	 * @return the condOp
	 */
	public CondOpEnum getCondOp() {
		return condOp;
	}
	/**
	 * @param condOp the condOp to set
	 */
	public void setCondOp(CondOpEnum condOp) {
		this.condOp = condOp;
	}
	/**
	 * @param child the child to set
	 */
	public void addChild(QueryTreeNode child) {
		this.children.add(child);
	}
	 
	public List<List<Term>> getFlattenedList(){
		List<List<Term>> flatList = new LinkedList<List<Term>>();
		if(this.condOp == CondOpEnum.OR){
			for(QueryTreeNode qtn:this.children){
				flatList.addAll(qtn.getFlattenedList());
			}
		} else if(this.condOp == CondOpEnum.AND){
			for(QueryTreeNode qtn:this.children){
				if(flatList.isEmpty()){
					flatList.addAll(qtn.getFlattenedList());
				} else {
				   flatList = multiplyFlatLists(flatList, qtn.getFlattenedList());
				}
			}
			
		}else{
			//TODO Error
		}
		return flatList;
	}
	private List<List<Term>> multiplyFlatLists(List<List<Term>> list1, List<List<Term>> list2){
		List<List<Term>> flatList = new LinkedList<List<Term>>();
		for(List<Term> l1:list1){
			for(List<Term> l2:list2){
				List<Term> newList = new LinkedList<Term>();
				newList.addAll(l1);
				newList.addAll(l2);
				flatList.add(newList);
			}
		}
		return flatList;
	}
}
