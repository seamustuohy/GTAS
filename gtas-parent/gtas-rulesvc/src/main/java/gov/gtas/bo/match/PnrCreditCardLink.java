/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.bo.match;

public class PnrCreditCardLink extends PnrAttributeLink {
    private static final long serialVersionUID = -1157351677154533276L;
    public PnrCreditCardLink(final long pnrId, final long creditCardId){
           super(pnrId, creditCardId);
       }
    /**
     * property access.
     * @return creditCard ID.
     */
    public long getCreditCardId(){
        return super.getLinkAttributeId();
    }
}
