package gov.gtas.bo.match;

public class PnrTravelAgencyLink extends PnrAttributeLink {
    private static final long serialVersionUID = 5014564222013081462L;
    public PnrTravelAgencyLink(final long pnrId, final long travelAgencyId){
           super(pnrId, travelAgencyId);
       }
    /**
     * property access.
     * @return address ID.
     */
    public long getTravelAgencyId(){
        return super.getLinkAttributeId();
    }
}
