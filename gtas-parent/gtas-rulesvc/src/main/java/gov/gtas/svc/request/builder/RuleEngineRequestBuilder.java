package gov.gtas.svc.request.builder;

import gov.gtas.bo.BasicRuleServiceRequest;
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
import gov.gtas.model.ApisMessage;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Document;
import gov.gtas.model.Email;
import gov.gtas.model.Flight;
import gov.gtas.model.FrequentFlyer;
import gov.gtas.model.Passenger;
import gov.gtas.model.Phone;
import gov.gtas.model.Pnr;
import gov.gtas.model.PnrMessage;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Rule Engine Request Builder constructs Rule Engine execution requests from
 * APIS and PNR messages. The constructed request contains all objects (e.g.,
 * passenger, flight) associated with the APIS and PNR messages supplied.
 * Duplicates are also removed in the construction process.
 * 
 * @author GTAS3 (AB)
 *
 */
public class RuleEngineRequestBuilder {
	private final List<Object> requestObjectList;
	private final Set<Long> passengerIdSet;
	private final Set<PnrPassengerLink> passengerLinkSet;
	private final Set<Long> flightIdSet;
	private final Set<Long> addressIdSet;
	private final Set<Long> phoneIdSet;
	private final Set<Long> emailIdSet;
	private final Set<Long> creditCardIdSet;
	private final Set<Long> frequentFlyerIdSet;
	private final Set<Long> travelAgencyIdSet;

	private RuleServiceRequestType requestType;

	public RuleEngineRequestBuilder() {
		this.requestObjectList = new LinkedList<Object>();
		this.addressIdSet = new HashSet<Long>();
		this.creditCardIdSet = new HashSet<Long>();
		this.emailIdSet = new HashSet<Long>();
		this.flightIdSet = new HashSet<Long>();
		this.frequentFlyerIdSet = new HashSet<Long>();
		this.passengerIdSet = new HashSet<Long>();
		this.passengerLinkSet = new HashSet<PnrPassengerLink>();
		this.phoneIdSet = new HashSet<Long>();
		this.travelAgencyIdSet = new HashSet<Long>();

		this.requestType = null;
	}

	public RuleServiceRequest build() {
		return new BasicRuleServiceRequest(requestObjectList, this.requestType);
	}

	/**
	 * Adds an Apis Message.
	 * 
	 * @param apisMessage
	 *            the message to add.
	 */
	public void addApisMessage(ApisMessage apisMessage) {
		// add flights, passengers and documents.
		// true for the second parameter means add passengers and documents
		addFlights(apisMessage.getFlights(), true);
		if (this.requestType == null
				|| this.requestType == RuleServiceRequestType.APIS_MESSAGE) {
			this.requestType = RuleServiceRequestType.APIS_MESSAGE;
		} else {
			this.requestType = RuleServiceRequestType.ANY_MESSAGE;
		}
	}

	/**
	 * Adds a PNR message and its associated components.
	 * 
	 * @param pnrMessage
	 *            the message to add.
	 */
	public void addPnrMessage(PnrMessage pnrMessage) {
		// add PNR objects
		Pnr pnr = pnrMessage.getPnr();
		if (pnr != null) {
			// add all the PNR related objects
			requestObjectList.add(pnr);
			addFlights(pnr.getFlights(), false);// false means do not add
												// passengers and documents.
			addAddressObjects(pnr, pnr.getAddresses());
			addPhoneObjects(pnr, pnr.getPhones());
			addEmailObjects(pnr, pnr.getEmails());
			addCreditCardObjects(pnr, pnr.getCreditCards());
			addFrequentFlyerObjects(pnr, pnr.getFrequentFlyers());
			addPassengerObjects(pnr, pnr.getPassengers());

			addTravelAgencyObject(pnr, pnr.getAgency());
		}
		if (this.requestType == null
				|| this.requestType == RuleServiceRequestType.PNR_MESSAGE) {
			this.requestType = RuleServiceRequestType.PNR_MESSAGE;
		} else {
			this.requestType = RuleServiceRequestType.ANY_MESSAGE;
		}
	}

	/**
	 * Adds flight objects to the builders list.
	 * 
	 * @param flights
	 *            the flights to add
	 * @param addAssociatedPassengers
	 *            if true adds the associated passengers and documents.
	 * @param addAssociatedPassengers
	 */
	private void addFlights(Collection<Flight> flights,
			boolean addAssociatedPassengers) {
		if (flights != null) {
			for (Flight flight : flights) {
				Long id = flight.getId();
				if (!this.flightIdSet.contains(id)) {
					this.requestObjectList.add(flight);
					this.flightIdSet.add(id);
				}
				if (addAssociatedPassengers) {
					addPassengerObjects(null, flight.getPassengers());
				}
			}
		}

	}

	private void addAddressObjects(final Pnr pnr,
			final Collection<Address> addresses) {
		if (addresses == null || addresses.isEmpty()) {
			return;
		}
		for (Address addr : addresses) {
			Long id = addr.getId();
			if (!this.addressIdSet.contains(id)) {
				requestObjectList.add(addr);
				requestObjectList.add(new PnrAddressLink(pnr.getId(), addr
						.getId()));
				this.addressIdSet.add(id);
			}
		}
	}

	private void addPhoneObjects(final Pnr pnr, final Collection<Phone> phones) {
		if (phones == null || phones.isEmpty()) {
			return;
		}
		for (Phone phone : phones) {
			Long id = phone.getId();
			if (!this.phoneIdSet.contains(id)) {
				requestObjectList.add(phone);
				requestObjectList.add(new PnrPhoneLink(pnr.getId(), phone
						.getId()));
				this.phoneIdSet.add(id);
			}
		}
	}

	private void addEmailObjects(final Pnr pnr, final Collection<Email> emails) {
		if (emails == null || emails.isEmpty()) {
			return;
		}
		for (Email email : emails) {
			long id = email.getId();
			if (!this.emailIdSet.contains(id)) {
				requestObjectList.add(email);
				requestObjectList.add(new PnrEmailLink(pnr.getId(), email
						.getId()));
				this.emailIdSet.add(id);
			}
		}
	}

	private void addFrequentFlyerObjects(final Pnr pnr,
			final Collection<FrequentFlyer> frequentFlyers) {
		if (frequentFlyers == null || frequentFlyers.isEmpty()) {
			return;
		}
		for (FrequentFlyer ff : frequentFlyers) {
			Long id = ff.getId();
			if (!this.frequentFlyerIdSet.contains(id)) {
				requestObjectList.add(ff);
				requestObjectList.add(new PnrFrequentFlyerLink(pnr.getId(), ff
						.getId()));
				this.frequentFlyerIdSet.add(id);
			}
		}
	}

	private void addCreditCardObjects(final Pnr pnr,
			final Collection<CreditCard> creditCards) {
		if (creditCards == null || creditCards.isEmpty()) {
			return;
		}
		for (CreditCard cc : creditCards) {
			Long id = cc.getId();
			if (!this.creditCardIdSet.contains(id)) {
				requestObjectList.add(cc);
				requestObjectList.add(new PnrCreditCardLink(pnr.getId(), cc
						.getId()));
				this.creditCardIdSet.add(id);
			}
		}
	}

	private void addTravelAgencyObject(final Pnr pnr, final Agency agency) {
		if (agency != null) {
			Long id = agency.getId();
			if (!this.travelAgencyIdSet.contains(id)) {
				requestObjectList.add(agency);
				requestObjectList.add(new PnrTravelAgencyLink(pnr.getId(),
						agency.getId()));
				this.travelAgencyIdSet.add(id);
			}
		}
	}

	/**
	 * Adds passenger and documents for PNR and APIS messages. In case of PNR a
	 * link object is also created.
	 * 
	 * @param pnr
	 *            the PNR object. If not null then a link object is also
	 *            created.
	 * @param passengers
	 *            the collection of passengers.
	 */
	private void addPassengerObjects(final Pnr pnr,
			final Collection<Passenger> passengers) {
		if (passengers == null || passengers.isEmpty()) {
			return;
		}
		for (Passenger passenger : passengers) {
			Long id = passenger.getId();
			if (!this.passengerIdSet.contains(id)) {
				requestObjectList.add(passenger);
				if (passenger.getDocuments() != null) {
					for (Document doc : passenger.getDocuments()) {
						this.requestObjectList.add(doc);
					}
				}
				this.passengerIdSet.add(id);
			}
			if (pnr != null) {
				addPnrPassengerLink(pnr, passenger);
			}
		}
	}

	private void addPnrPassengerLink(final Pnr pnr, final Passenger passenger) {
		PnrPassengerLink link = new PnrPassengerLink(pnr.getId(),
				passenger.getId());
		if (!this.passengerLinkSet.contains(link)) {
			requestObjectList.add(link);
			this.passengerLinkSet.add(link);
		}

	}
}
