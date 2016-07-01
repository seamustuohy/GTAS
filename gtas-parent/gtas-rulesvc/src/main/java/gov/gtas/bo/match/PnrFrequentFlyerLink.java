/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.bo.match;

public class PnrFrequentFlyerLink extends PnrAttributeLink {

    /**
     * serial version UID
     */
    private static final long serialVersionUID = 6781453461421106156L;
    public PnrFrequentFlyerLink(final long pnrId, final long frequentFlierId){
       super(pnrId, frequentFlierId);
}
/**
* property access.
* @return frequent flier ID.
*/
public long getFrequentFlierId(){
    return super.getLinkAttributeId();
}
}
