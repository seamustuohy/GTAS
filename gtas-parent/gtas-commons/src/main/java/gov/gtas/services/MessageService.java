package gov.gtas.services;

import gov.gtas.model.ApisMessage;
import gov.gtas.model.Message;

import java.util.Date;
import java.util.List;

public interface MessageService {

    public List<Message> getAPIsByDates (Date startDate, Date endDate);

}
