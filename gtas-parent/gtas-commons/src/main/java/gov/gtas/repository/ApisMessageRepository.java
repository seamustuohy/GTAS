package gov.gtas.repository;

import gov.gtas.model.ApisMessage;
import gov.gtas.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ApisMessageRepository extends MessageRepository<ApisMessage> {


    @Query("SELECT apis FROM ApisMessage apis WHERE apis.createDate >= CURRENT_DATE-1 AND apis.createDate <= CURRENT_DATE")
    public List<Message> getAPIsByDates();
}