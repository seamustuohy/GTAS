/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.bo;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

public class BasicRuleServiceRequest implements RuleServiceRequest,
        Serializable {

    private static final long serialVersionUID = 8527612411998852833L;

    private Collection<Object> requestObjects;
    private RuleServiceRequestType requestType;
    
    /**
     * Constructs an object using defaults.
     */
    public BasicRuleServiceRequest(){
        this.requestObjects = new LinkedList<Object>();
        this.requestType = RuleServiceRequestType.ANY_MESSAGE;
    }
    /**
     * Constructs a request object using provided parameters.
     * @param requestColl the collection of request objects.
     * @param type the type of request.
     */
    public BasicRuleServiceRequest(final Collection<Object> requestColl, final RuleServiceRequestType type){
        this.requestObjects = requestColl;
        this.requestType = type;
    }
    /**
     * Adds a request object to this request.
     * @param reqObj the request object to add.
     */
    public void addRequestObject(final Object reqObj){
        this.requestObjects.add(reqObj);
    }
    /**
     * Adds a collection of request objects to this request.
     * @param reqObjects the collection of request objects to add.
     */
    public void addRequestObjects(final Collection<Object> reqObjects){
        this.requestObjects.add(reqObjects);
    }
    @Override
    public Collection<?> getRequestObjects() {
        return requestObjects;
    }

    @Override
    public RuleServiceRequestType getRequestType() {
        return requestType;
    }

}
