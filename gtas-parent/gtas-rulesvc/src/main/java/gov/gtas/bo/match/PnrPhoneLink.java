/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.bo.match;

public class PnrPhoneLink extends PnrAttributeLink {
    private static final long serialVersionUID = -5957136488500600885L;
    public PnrPhoneLink(final long pnrId, final long phoneId){
           super(pnrId, phoneId);
       }
    /**
     * property access.
     * @return phone ID.
     */
    public long getPhoneId(){
        return super.getLinkAttributeId();
    }
}
