package gov.gtas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import gov.gtas.model.Pnr;

public interface PnrRepository extends CrudRepository<Pnr, Long> {
    @Query("select pnr from Pnr pnr join pnr.passengers pax join pnr.flights f where pax.id = :passengerId and f.id = :flightId")
    public List<Pnr> getPnrsByPassengerIdAndFlightId(@Param("passengerId") Long passengerId,
    									@Param("flightId") Long flightId);
    
    @Query("select pnr from Pnr pnr join pnr.passengers pax where pax.id = :passengerId")
    public List<Pnr> getPnrsByPassengerId(@Param("passengerId") Long passengerId);
}
