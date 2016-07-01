/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.bo.match;

public class PnrEmailLink extends PnrAttributeLink {
    private static final long serialVersionUID = -4878307401317536141L;
    public PnrEmailLink(final long pnrId, final long emailId){
           super(pnrId, emailId);
       }
    /**
     * property access.
     * @return email ID.
     */
    public long getEmailId(){
        return super.getLinkAttributeId();
    }
}
