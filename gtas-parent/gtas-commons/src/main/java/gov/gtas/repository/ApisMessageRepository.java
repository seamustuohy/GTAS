package gov.gtas.repository;

import gov.gtas.model.ApisMessage;
import gov.gtas.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ApisMessageRepository extends MessageRepository<ApisMessage> {

    @Query("SELECT apis FROM Message apis WHERE apis.createDate between :startDate AND :endDate")
    public List<Message> getAPIsByDates(@Param("startDate") Date startDate,
                                        @Param("endDate") Date endDate);
}