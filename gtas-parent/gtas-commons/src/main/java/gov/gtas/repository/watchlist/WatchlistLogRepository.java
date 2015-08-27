package gov.gtas.repository.watchlist;

import gov.gtas.model.watchlist.WatchlistEditLog;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Watch list Repository with custom queries.
 * 
 * @author GTAS3 (AB)
 *
 */
public interface WatchlistLogRepository extends
		CrudRepository<WatchlistEditLog, Long>,
		JpaSpecificationExecutor<WatchlistEditLog> {
	@Query("SELECT log FROM WatchlistEditLog log WHERE log.editedWatchlist.watchlistName = :watchlistName")
	public List<WatchlistEditLog> getLogByWatchlistName(
			@Param("watchlistName") String watchlistName);

	@Query("SELECT log FROM WatchlistEditLog log WHERE log.editedWatchlist.watchlistName = :watchlistName and log.editTimestamp >= :editTimestamp")
	public List<WatchlistEditLog> getLogByWatchlistNameDateGreater(
			@Param("watchlistName") String watchlistName,
			@Param("editTimestamp") Date editTimestamp);

	@Query("SELECT log FROM WatchlistEditLog log WHERE log.editedWatchlist.watchlistName = :watchlistName and log.editTimestamp between :fromDate and :toDate")
	public List<WatchlistEditLog> getLogByWatchlistNameDateBetween(
			@Param("watchlistName") String watchlistName,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}
