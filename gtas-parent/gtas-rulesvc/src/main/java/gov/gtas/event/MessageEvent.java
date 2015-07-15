package gov.gtas.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {

	private static final long serialVersionUID = 4362059701460790885L;
	private List<Long> messageIds = new ArrayList<Long>();

	public MessageEvent(Object source, List<Long> idList) {
		super(source);
		this.messageIds = idList;

	}

	public List<Long> getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(List<Long> messageIds) {
		this.messageIds = messageIds;
	}

}
