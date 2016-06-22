package gov.gtas.bo.match;

public class PnrAddressLink extends PnrAttributeLink {
   /**
     * serial version UID
     */
    private static final long serialVersionUID = 4794542237529461610L;

    public PnrAddressLink(final long pnrId, final long addressId){
           super(pnrId, addressId);
       }
    /**
     * property access.
     * @return address ID.
     */
    public long getAddressId(){
        return super.getLinkAttributeId();
    }
}
