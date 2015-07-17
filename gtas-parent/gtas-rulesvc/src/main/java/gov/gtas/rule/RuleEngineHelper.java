package gov.gtas.rule;

import gov.gtas.bo.RuleExecutionStatistics;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.bo.RuleServiceRequestType;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;

import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieSession;

public class RuleEngineHelper {
	/**
	 * Adds events listeners to the Kie Session.
	 * 
	 * @param ksession
	 *            the session to add listeners to.
	 * @param eventListenerList
	 *            the list of event listeners.
	 */
	public static void addEventListenersToKieSEssion(final KieSession ksession,
			final List<EventListener> eventListenerList) {
		// The application can also setup listeners
		if (eventListenerList != null) {
			for (EventListener el : eventListenerList) {
				if (el instanceof AgendaEventListener) {
					ksession.addEventListener((AgendaEventListener) el);
				} else if (el instanceof RuleRuntimeEventListener) {
					ksession.addEventListener((RuleRuntimeEventListener) el);
				}
			}
		}
	}

	/**
	 * Creates a request from an arbitrary object.
	 * 
	 * @param req
	 *            the input object.
	 * @return RuleServiceRequest object.
	 */
	public static RuleServiceRequest createRequest(
			final gov.gtas.model.Message req) {
		final List<gov.gtas.model.Message> requestList = new ArrayList<gov.gtas.model.Message>();
		requestList.add(req);
		return new RuleServiceRequest() {
			public List<?> getRequestObjects() {
				return requestList;
			}

			public RuleServiceRequestType getRequestType() {
				return RuleServiceRequestType.ANY_MESSAGE;
			}

		};
	}

	/**
	 * Creates a request from a API message.
	 * 
	 * @param req
	 *            the API message.
	 * @return RuleServiceRequest object.
	 */
	public static RuleServiceRequest createApisRequest(final ApisMessage req) {
		final List<Flight> requestList = new ArrayList<Flight>(req.getFlights());
		return new RuleServiceRequest() {
			public List<?> getRequestObjects() {
				return requestList;
			}

			public RuleServiceRequestType getRequestType() {
				return RuleServiceRequestType.APIS_MESSAGE;
			}

		};
	}

	/**
	 * Creates a list of KieSession event listeners.
	 * 
	 * @param stats
	 *            the data structure to accumulate rule execution statistics.
	 * @return list of event listeners.
	 */
	public static List<EventListener> createEventListeners(
			final RuleExecutionStatistics stats) {
		List<EventListener> eventListenerList = new LinkedList<EventListener>();

		eventListenerList.add(new DebugAgendaEventListener() {

			@Override
			public void afterMatchFired(AfterMatchFiredEvent event) {
				stats.incrementTotalRulesFired();
				stats.addRuleFired(event.getMatch().getRule().getName());
				super.afterMatchFired(event);
			}

		});
		eventListenerList.add(new DebugRuleRuntimeEventListener() {

			@Override
			public void objectUpdated(ObjectUpdatedEvent event) {
				stats.incrementTotalObjectsModified();
				stats.addModifiedObject(event.getObject());
				super.objectUpdated(event);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.kie.api.event.rule.DebugRuleRuntimeEventListener#objectDeleted
			 * (org.kie.api.event.rule.ObjectDeletedEvent)
			 */
			@Override
			public void objectDeleted(ObjectDeletedEvent event) {
				stats.incrementTotalObjectsModified();
				stats.addDeletedObject(event.getOldObject());
				super.objectDeleted(event);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.kie.api.event.rule.DebugRuleRuntimeEventListener#objectInserted
			 * (org.kie.api.event.rule.ObjectInsertedEvent)
			 */
			@Override
			public void objectInserted(ObjectInsertedEvent event) {
				stats.incrementTotalObjectsModified();
				stats.addInsertedObject(event.getObject());
				super.objectInserted(event);
			}

		});

		return eventListenerList;
	}

}
