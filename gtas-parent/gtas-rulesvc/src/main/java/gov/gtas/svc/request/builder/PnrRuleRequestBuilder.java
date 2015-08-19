package gov.gtas.svc.request.builder;

import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.bo.RuleServiceRequestType;
import gov.gtas.bo.match.PnrAddressLink;
import gov.gtas.bo.match.PnrCreditCardLink;
import gov.gtas.bo.match.PnrEmailLink;
import gov.gtas.bo.match.PnrFrequentFlyerLink;
import gov.gtas.bo.match.PnrPassengerLink;
import gov.gtas.bo.match.PnrPhoneLink;
import gov.gtas.bo.match.PnrTravelAgencyLink;
import gov.gtas.model.Address;
import gov.gtas.model.Agency;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Email;
import gov.gtas.model.FrequentFlyer;
import gov.gtas.model.Passenger;
import gov.gtas.model.Phone;
import gov.gtas.model.Pnr;
import gov.gtas.model.PnrMessage;
import gov.gtas.svc.TargetingServiceUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PnrRuleRequestBuilder {
	private final List<Object> requestObjectList;

	public PnrRuleRequestBuilder() {
		this.requestObjectList = new LinkedList<Object>();
	}

	public RuleServiceRequest build() {
		return TargetingServiceUtils
				.createRuleServiceRequest(RuleServiceRequestType.PNR_MESSAGE,
						requestObjectList);
	}

	public void addPnrMessage(PnrMessage pnrMessage) {
		if (pnrMessage == null) {
			return;
		}
		// add PNR objects
		Set<Pnr> pnrSet = pnrMessage.getPnrs();
		if (pnrSet != null && !pnrSet.isEmpty()) {
			// add all the PNR related objects
			for (Pnr pnr : pnrSet) {
				requestObjectList.add(pnr);
				if(pnr.getFlights() != null){
					requestObjectList.addAll(pnr.getFlights());
				}
				addAddressObjects(pnr, pnr.getAddresses());
				addPhoneObjects(pnr, pnr.getPhones());
				addEmailObjects(pnr, pnr.getEmails());
				addCreditCardObjects(pnr, pnr.getCreditCards());
				addFrequentFlyerObjects(pnr, pnr.getFrequentFlyers());
				addPassengerObjects(pnr, pnr.getPassengers());

				addTravelAgencyObject(pnr, pnr.getAgency());
			}
		}
	}

	private void addAddressObjects(final Pnr pnr,
			final Collection<Address> addresses) {
		if (addresses == null || addresses.isEmpty()) {
			return;
		}
		for (Address addr : addresses) {
			requestObjectList.add(addr);
			requestObjectList
					.add(new PnrAddressLink(pnr.getId(), addr.getId()));
		}
	}

	private void addPhoneObjects(final Pnr pnr, final Collection<Phone> phones) {
		if (phones == null || phones.isEmpty()) {
			return;
		}
		for (Phone phone : phones) {
			requestObjectList.add(phone);
			requestObjectList.add(new PnrPhoneLink(pnr.getId(), phone.getId()));
		}
	}

	private void addEmailObjects(final Pnr pnr, final Collection<Email> emails) {
		if (emails == null || emails.isEmpty()) {
			return;
		}
		for (Email email : emails) {
			requestObjectList.add(email);
			requestObjectList.add(new PnrEmailLink(pnr.getId(), email.getId()));
		}
	}

	private void addFrequentFlyerObjects(final Pnr pnr,
			final Collection<FrequentFlyer> frequentFlyers) {
		if (frequentFlyers == null || frequentFlyers.isEmpty()) {
			return;
		}
		for (FrequentFlyer ff : frequentFlyers) {
			requestObjectList.add(ff);
			requestObjectList.add(new PnrFrequentFlyerLink(pnr.getId(), ff
					.getId()));
		}
	}

	private void addCreditCardObjects(final Pnr pnr,
			final Collection<CreditCard> creditCards) {
		if (creditCards == null || creditCards.isEmpty()) {
			return;
		}
		for (CreditCard cc : creditCards) {
			requestObjectList.add(cc);
			requestObjectList
					.add(new PnrCreditCardLink(pnr.getId(), cc.getId()));
		}
	}

	private void addTravelAgencyObject(final Pnr pnr, final Agency agency) {
		if (agency != null) {
			requestObjectList.add(agency);
			requestObjectList.add(new PnrTravelAgencyLink(pnr.getId(), agency
					.getId()));
		}
	}

	private void addPassengerObjects(final Pnr pnr,
			final Collection<Passenger> passengers) {
		if (passengers == null || passengers.isEmpty()) {
			return;
		}
		for (Passenger passenger : passengers) {
			requestObjectList.add(passenger);
			requestObjectList.add(new PnrPassengerLink(pnr.getId(), passenger
					.getId()));
		}
	}
}
