package gov.gtas.bo.match;

public class PnrPassengerLink extends PnrAttributeLink {
    private static final long serialVersionUID = 7360778064356140448L;
    public PnrPassengerLink(final long pnrId, final long passengerId){
           super(pnrId, passengerId);
       }
    /**
     * property access.
     * @return passenger ID.
     */
    public long getPassengerId(){
        return super.getLinkAttributeId();
    }
}
